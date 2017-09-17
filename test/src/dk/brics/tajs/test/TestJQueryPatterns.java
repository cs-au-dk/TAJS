package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

/**
 * jQuery code patterns, carefully extracted from the jQuery library to provide small test cases. The analysis should confirm that all assert-statements are true
 */
public class TestJQueryPatterns {

    @Before
    public void before() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableIncludeDom();
        Options.get().enableDeterminacy();
    }

    @Test
    public void jQuery111_each() {
        Misc.run("test-resources/src/jquery-patterns/jquery-1.11_each.js");
    }

    @Test
    public void jQuery111_each_arrayIterationClosure() {
        Misc.run("test-resources/src/jquery-patterns/jquery-1.11_each.js", "test-resources/src/jquery-patterns/jQuery-1.11_each_arrayIterationClosure.js");
    }

    @Test
    public void jQuery111_each_objectIterationClosure() {
        Misc.run("test-resources/src/jquery-patterns/jquery-1.11_each.js", "test-resources/src/jquery-patterns/jquery-1.11_each_objectIterationClosure.js");
    }

    @Test
    public void jQuery111_each_nestedObjectCreationIterationClosure() {
        Misc.run("test-resources/src/jquery-patterns/jquery-1.11_each.js", "test-resources/src/jquery-patterns/jquery-1.11_each_nestedObjectCreationIterationClosure.js");
    }

    @Test
    public void jQuery111_each_jQuery17_arrayMutation() {
        Misc.run("test-resources/src/jquery-patterns/jquery-1.11_each.js", "test-resources/src/jquery-patterns/jquery-1.7_each_arrayThisIteration.js");
    }

    @Test
    public void jQuery111_extend_selfExtend() {
        Misc.run("test-resources/src/jquery-patterns/jquery-1.11_extend.js", "test-resources/src/jquery-patterns/jquery-1.11_extend_selfExtend.js");
    }

    @Test
    public void jQuery111_extend_deepExtend() {
        Misc.run("test-resources/src/jquery-patterns/jquery-1.11_extend.js", "test-resources/src/jquery-patterns/jquery-1.11_extend_deepExtend.js");
    }

    @Test
    public void jQuery11_each() {
        Misc.run("test-resources/src/jquery-patterns/jquery-1.1_each.js");
    }

    /**
     * This is not really extracted from jquery -- but it is a small example of precision that other tests need
     */
    @Test
    public void heap_and_call_sensitivity() {
        Misc.run("test-resources/src/jquery-patterns/heap-and-call-sensitive-closure.js");
    }
}
