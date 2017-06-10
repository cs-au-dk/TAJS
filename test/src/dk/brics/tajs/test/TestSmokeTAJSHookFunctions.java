package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.nativeobjects.TAJSFunction;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class TestSmokeTAJSHookFunctions {

    @Parameterized.Parameter
    public TAJSFunction target;

    @Parameterized.Parameters(name = "{0}")
    public static List<TAJSFunction> getData() {
        return Arrays.asList(TAJSFunction.values()).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Before
    public void before() {
        Main.initLogging();
        Main.reset();
        Options.get().enableTest();
        Options.get().enableIncludeDom();
        Options.get().enableAsyncEvents();
    }

    @Test
    public void hookNameIsNotGlobal() throws Exception {
        Misc.runSource(target.toString(),
                        String.format("TAJS_assert(typeof %s === 'undefined');", target.toString()));
    }

    @Test
    public void canAssign() throws Exception {
        Misc.runSource(target.toString(),
                        String.format("var %s = 42; TAJS_assert(%s === 42);", target.toString(), target.toString()));
    }
}
