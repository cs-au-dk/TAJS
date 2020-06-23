package dk.brics.tajs.test;
// import static org.junit.Assert.fail;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestWala {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestWala");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        // Options.get().enableNoLazy();
    }

    @Test
    public void wala_forin() throws Exception {
        String[] args = {"test-resources/src/wala/forin.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_forinct() throws Exception {
        String[] args = {"test-resources/src/wala/forin.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_functions() throws Exception {
        String[] args = {"test-resources/src/wala/functions.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_inherit() throws Exception {
        String[] args = {"test-resources/src/wala/inherit.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_more_control_flow() throws Exception {
        String[] args = {"test-resources/src/wala/more-control-flow.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_newfn() throws Exception {
        Options.get().enableUnevalizer();
        String[] args = {"test-resources/src/wala/newfn.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_objects() throws Exception {
        String[] args = {"test-resources/src/wala/objects.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_portal_example_simple() throws Exception {
        String[] args = {"test-resources/src/wala/portal-example-simple.html"};
        Misc.run(args);
    }

    @Test
    public void wala_simple_lexical() throws Exception {
        String[] args = {"test-resources/src/wala/simple-lexical.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_simple() throws Exception {
        String[] args = {"test-resources/src/wala/simple.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_simpler() throws Exception {
        String[] args = {"test-resources/src/wala/simpler.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_string_op() throws Exception {
        String[] args = {"test-resources/src/wala/string-op.js"}; //note: bug in assumption
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_string_prims() throws Exception {
        String[] args = {"test-resources/src/wala/string-prims.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_try() throws Exception {
        String[] args = {"test-resources/src/wala/try.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_upward() throws Exception {
        Options.get().enableNoPolymorphic(); // free variable partitioning kills polymorphic value, so disable polymorphic values to get predictable output
        String[] args = {"test-resources/src/wala/upward.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void wala_args() throws Exception {
        String[] args = {"test-resources/src/wala/args.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }
}
