package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.nativeobjects.ECMAScriptObjects;
import dk.brics.tajs.analysis.nativeobjects.JSGlobal;
import dk.brics.tajs.lattice.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.monitoring.OrdinaryExitReachableChecker;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class TestSmokeTAJSHookFunctions {

    @Parameterized.Parameter
    public ECMAScriptObjects target;

    @Parameterized.Parameters(name = "{0}")
    public static List<ECMAScriptObjects> getData() {
        return Arrays.asList(ECMAScriptObjects.values()).stream()
                .filter(o -> o.toString().startsWith("TAJS_"))
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
    public void hookIsKnown() throws Exception {
        Value value = Value.makeObject(new ObjectLabel(target, ObjectLabel.Kind.FUNCTION));
        assertTrue(JSGlobal.removeTAJSSpecificFunctions(value).isNone());
    }

    @Test
    public void hookNameIsGlobal() throws Exception {
        Misc.runSource(target.toString(), new String[]{
                        String.format("%s;", target.toString()),
                },
                new CompositeMonitoring(new Monitoring(), new OrdinaryExitReachableChecker()));
    }

    @Test
    public void hookIsFunction() throws Exception {
        Misc.runSource(target.toString(), new String[]{
                        String.format("TAJS_assert(typeof %s === 'function');", target.toString()),
                },
                new CompositeMonitoring(new Monitoring(), new OrdinaryExitReachableChecker()));
    }
}
