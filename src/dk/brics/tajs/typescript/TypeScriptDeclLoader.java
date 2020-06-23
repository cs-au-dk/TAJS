/*
 * Copyright 2009-2020 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.brics.tajs.typescript;

import dk.au.cs.casa.typescript.SpecReader;
import dk.au.cs.casa.typescript.types.Type;
import dk.brics.tajs.analysis.Solver;
import dk.brics.tajs.analysis.nativeobjects.FunctionFileLoader;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.options.ExternalDependencies;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.options.TAJSEnvironmentConfig;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.Loader;
import dk.brics.tajs.util.PathAndURLUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

import static dk.brics.tajs.util.Collections.newMap;

//import org.apache.log4j.Level;
//import org.apache.log4j.LogManager;

/**
 * Loads TypeScript declaration files.
 */
public class TypeScriptDeclLoader {

    private static Logger log = Logger.getLogger(TypeScriptDeclLoader.class);

//    static {
//        LogManager.getLogger(TypeScriptDeclLoader.class).setLevel(Level.DEBUG);
//    }

    private static SpecReader env; // standard type environment

    private static Map<Type, ObjectLabel.Kind> stdTypes = newMap(); // standard types

    /**
     * Attempts to find a TypeScript declaration file for the given JavaScript module, returns null if not found.
     */
    public static Type loadModuleType(String target, Solver.SolverInterface c) {
        return loadTypeScriptDeclarationFile(FunctionFileLoader.getURL(target, c));
    }

    /**
     * Loads the type declaration file for the given library, or returns null if not found.
     */
    private static Type loadTypeScriptDeclarationFile(URL libfile) {
        if (libfile.getProtocol().equals("tajs-host-env"))
            return null;
        if (!libfile.getProtocol().equals("file")) {
            log.debug("Can't load TypeScript declaration file for non-file URL " + libfile);
            return null;
        }
        if (!libfile.toString().endsWith(".js")) {
            log.debug("Can't load TypeScript declaration file for non-.js file " + libfile);
            return null;
        }
        try {
            Path libfilepath = PathAndURLUtils.toPath(libfile, false);
            Path dtsjsonfile = generateJSONDeclFile(libfilepath);
            if (dtsjsonfile == null)
                return null;
            if (log.isDebugEnabled())
                log.debug("Loading TypeScript declaration file " + dtsjsonfile);
            SpecReader spec = new SpecReader(Loader.getString(dtsjsonfile, StandardCharsets.UTF_8));
            String basename = PathAndURLUtils.removeExtension(libfilepath.getFileName());
            findStandardTypes(spec);
            return findModuleType(spec, dtsjsonfile, basename);
        } catch (IOException | InterruptedException e) {
            throw new AnalysisException(e);
        }
    }

    private static void findStandardTypes(SpecReader spec) {
        for (SpecReader.NamedType nt : spec.getNamedTypes()) {
            for (ObjectLabel.Kind k : ObjectLabel.Kind.values())
                if (nt.qName.contains(k.toString()))
                    stdTypes.put(nt.type, k);
        }
        log.debug("Standard types: " + stdTypes);
    }

    public static Map<Type, ObjectLabel.Kind> getStdTypes() {
        return stdTypes;
    }

    private static Type findModuleType(SpecReader spec, Path dtsjsonfile, String basename) throws IOException {
        // look for single ambient named basename
        for (SpecReader.NamedType n : spec.getAmbientTypes())
            if (n.qName.size() == 1 && n.qName.get(0).equals(basename))
                return n.type;
        // load empty environment (if not done already)
        if (env == null)
            env = new SpecReader(Loader.getString(TypeScriptDeclLoader.class.getResource("/tsspecs/es6-dom.json"), StandardCharsets.UTF_8));
        // look for user-defined global property
        Map<String, Type> userDefinedTypes = newMap();
        for (Map.Entry<String, Type> entry : spec.getGlobal().getDeclaredProperties().entrySet())
            if (!env.getGlobal().getDeclaredProperties().containsKey(entry.getKey()) && !entry.getKey().equals("global"))
                userDefinedTypes.put(entry.getKey(), entry.getValue());
        if (userDefinedTypes.size() == 1)
            return userDefinedTypes.values().stream().findFirst().get();
        throw new AnalysisException("Unable to resolve module type in " + dtsjsonfile);
    }

    /**
     * If the .d.ts.json file is missing or outdated, attempt to (re-)generate it.
     *
     * @param libfilepath path of library file
     * @return URL of .d.t.json file
     */
    private static Path generateJSONDeclFile(Path libfilepath) throws IOException, InterruptedException {
        String libfilestrnoext = PathAndURLUtils.removeExtension(libfilepath);
        // first look in directory containing the .js file
        String dtsfile = libfilestrnoext + ".d.ts";
        File dts = new File(dtsfile);
        if (!dts.exists()) {
            // then look in @types in node_modules ancestor directories
            dts = null;
            Path nodemodules = libfilepath;
            while (nodemodules != null && nodemodules.getFileName() != null && !nodemodules.getFileName().toString().equals("node_modules"))
                nodemodules = nodemodules.getParent();
            if (nodemodules != null && nodemodules.getFileName() != null) {
                Path relative = nodemodules.relativize(libfilepath).getParent();
                dts = nodemodules.resolve("@types").resolve(relative).resolve("index.d.ts").toFile();
                if (dts.exists())
                    dtsfile = dts.toString();
                else
                    dts = null;
            }
            if (dts == null) {
                log.debug("No TypeScript declaration file found for module " + libfilepath);
                return null;
            }
        }
        // convert .d.ts to .d.ts.json if necessary
        File dtsjson = new File(dtsfile + ".json");
        if (!dtsjson.exists() || dts.lastModified() >= dtsjson.lastModified()) { // TODO: timestamp useless if files checked out together :-(
            if (!Options.get().isTestEnabled())
                log.info("Converting " + dtsfile + " to .json");
            String[] cmd = {
                    TAJSEnvironmentConfig.get().getNode().toString(),
                    ExternalDependencies.getTSSpecReaderDirectory().orElseGet(() -> {
                        throw new AnalysisException("Can't find ts-spec-reader directory!?");
                    }).resolve("src/CLI.js").toString(), // TODO: possible to load as resource (from tajs-all.jar)?
                    dtsfile,
                    "--env",
                    "es6-dom",
                    "-o",
                    dtsjson.toString()};
            log.debug(">>> " + String.join(" ", cmd));
            Process p = Runtime.getRuntime().exec(cmd);
            if (p.waitFor() != 0) {
                String err = new BufferedReader(new InputStreamReader(p.getErrorStream())).lines().collect(Collectors.joining("\n"));
                log.error("Error:\n" + err);
                throw new AnalysisException("Unable to convert .d.ts file " + dts);
            }
        }
        return dtsjson.toPath();
    }
}
