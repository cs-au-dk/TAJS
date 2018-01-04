package dk.brics.tajs.test.nodejs;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.Misc;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class TestNodeJSRequire {

    @Before
    public void before() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        Options.get().enableUnevalizer();
        Options.get().enableAsyncEvents();
        Options.get().enableDeterminacy();
        Options.get().enableNodeJS();
        Options.get().getSoundnessTesterOptions().setRootDirFromMainDirectory(Paths.get("."));
    }

    @Test
    public void testRequireProperties() {
        Misc.runSource("TAJS_assert(require, 'isMaybeObject');",
                "TAJS_assert(require.extensions, 'isMaybeObject');",
                "TAJS_assert(require.cache, 'isMaybeObject');");
    }

    @Test
    public void requireModule() {
        Misc.run("test-resources/src/nodejs/require/require-module/require-module.js");
    }

    @Test
    public void requireWithExportedModule() {
        Misc.run("test-resources/src/nodejs/require/require-with-exported-module/require-with-exported-module.js");
    }

    @Test
    public void requireMultiple() {
        Misc.run("test-resources/src/nodejs/require/require-multiple/a.js");
    }

    @Test
    public void requireRelativeTwice() {
        Misc.run("test-resources/src/nodejs/require/require-relative-twice/a.js");
    }

    @Test
    public void requireRelativeTwiceIndirectly() {
        Misc.run("test-resources/src/nodejs/require/require-relative-twice-indirectly/a.js");
    }

    @Test
    public void requireRelativeTwiceIndirectlyWithExport() {
        Misc.run("test-resources/src/nodejs/require/require-relative-twice-indirectly-with-export/a.js");
    }

    @Test
    public void requireRelative() {
        Misc.run("test-resources/src/nodejs/require/require-relative/require-relative.js");
    }

    @Test
    public void requireNested() {
        Misc.run("test-resources/src/nodejs/require/require-nested/main.js");
    }

    @Test
    public void requireBuiltin() {
        Misc.run("test-resources/src/nodejs/require/require-builtins/os.js");
    }
}
