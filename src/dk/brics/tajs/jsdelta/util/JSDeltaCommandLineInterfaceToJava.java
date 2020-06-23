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

package dk.brics.tajs.jsdelta.util;

import dk.brics.tajs.jsdelta.DeltaMinimizer;
import dk.brics.tajs.jsdelta.RunPredicate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Command line interface used by an external JSDelta process for invoking TAJS.
 */
public class JSDeltaCommandLineInterfaceToJava {

    /**
     * Invoked from jsdelta (because of {@link DeltaMinimizer#reduce(Path, Path, Class, String, boolean, boolean, boolean)}  with the canonical name of a JSDeltaTester and the Path to test
     */
    public static void main(String... args) {
        if (args.length < 2 || args.length > 3) {
            System.err.println("Arguments missing");
            System.exit(3);
        }
        String testerClassName = args[0];
        String testerArg = args.length == 3 ? args[1] : null;
        String mainFile = args.length == 3 ? args[2] : args[1];
        try {
            boolean result = performTestFromStringArgs(testerClassName, testerArg, mainFile);
            System.exit(result ? 0 : 1);
        } catch (Throwable t) {
            System.err.println("Unexpected failure when testing:");
            t.printStackTrace();
            System.exit(2);
        }
    }

    public static boolean performTestFromStringArgs(String testerClassName, String testerArg, String mainFile) throws ClassNotFoundException {
        @SuppressWarnings("unchecked")
        Class<? extends RunPredicate> testerClass = (Class<? extends RunPredicate>) Class.forName(testerClassName);
        RunPredicate tester = instantiateTester(testerClass, testerArg);
        return tester.test(Paths.get(mainFile));
    }

    public static RunPredicate instantiateTester(Class<? extends RunPredicate> testerClass, String testerArg) {
        try {
            if (null == testerArg || "null".equals(testerArg)) {
                return testerClass.getDeclaredConstructor().newInstance();
            } else {
                Constructor<? extends RunPredicate> argConstructor = testerClass.getDeclaredConstructor(String.class);
                return argConstructor.newInstance(testerArg);
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
