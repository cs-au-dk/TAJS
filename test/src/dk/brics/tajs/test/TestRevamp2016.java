package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

public class TestRevamp2016 {

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
    }

    @Test
    public void Revamp2016_jquery_app0() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/jquery/jquery_app0.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_jquery_app1() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/jquery/jquery_app1.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_jquery_app2() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/jquery/jquery_app2.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_jquery_app3() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/jquery/jquery_app3.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_jquery_app4() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/jquery/jquery_app4.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_jquery_tutorial_example_a() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/jquery/jquery_tutorial_example_a.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_jquery_tutorial_example_b() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/jquery/jquery_tutorial_example_b.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_jquery_tutorial_example_c() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/jquery/jquery_tutorial_example_c.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_jquery_tutorial_example_d() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/jquery/jquery_tutorial_example_d.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_jquery_tutorial_example_e() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/jquery/jquery_tutorial_example_e.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_jquery_tutorial_example_f() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/jquery/jquery_tutorial_example_f.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_prototype_ajax() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/prototype/ajax.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_prototype_classes() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/prototype/classes.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_prototype_justload() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/prototype/justload.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_prototype_observe() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/prototype/observe.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_prototype_query() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/prototype/query.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_prototype_trythese() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/prototype/trythese.html"};
        Misc.run(args);
    }

    @Test
    public void Revamp2016_scriptaculous_justload() throws Exception {
        Misc.init();
        String[] args = {"test/revamp/scriptaculous/justload.html"};
        Misc.run(args);
    }
}
