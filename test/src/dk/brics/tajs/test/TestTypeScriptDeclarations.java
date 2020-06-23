package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestTypeScriptDeclarations {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestTypeScriptDeclarations");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().disableTestSoundness();
//        Options.get().getSoundnessTesterOptions().setRootDirFromMainDirectory(Paths.get("../../"));
//        Options.get().getSoundnessTesterOptions().setGenerateOnlyIncludeAutomaticallyForHTMLFiles(true);

        Options.get().enableDeterminacy();
        Options.get().enablePolyfillMDN();
        Options.get().enablePolyfillTypedArrays();
        Options.get().enablePolyfillES6Collections();
        Options.get().enablePolyfillES6Promises();
        Options.get().enableConsoleModel();
        Options.get().enableIncludeDom();

//        Options.get().enableBlendedAnalysis();
//        Options.get().enableIgnoreUnreached();

        Options.get().enableNodeJS();
        Options.get().enableTypeFiltering();
    }

    @Test
    public void myapp() {
        Misc.run("test-resources/src/tsspecs/myapp/myapp.js");
        Misc.checkSystemOutput();
    }

    @Ignore // TODO: needs models for Node.js
    @Test
    public void lodashtest() {
        Misc.run("test-resources/src/tsspecs/lodashtest/app.js");
        Misc.checkSystemOutput();
    }

    @Test
    public void underscoretest() {
        Misc.run("test-resources/src/tsspecs/underscoretest/app.js");
        Misc.checkSystemOutput();
    }
}
