package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.monitoring.CompositeMonitoring;
import dk.brics.tajs.monitoring.IAnalysisMonitoring;
import dk.brics.tajs.monitoring.Monitoring;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.test.monitors.OrdinaryExitReachableCheckerMonitor;
import dk.brics.tajs.util.AnalysisException;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("static-method")
public class TestUneval {

    private IAnalysisMonitoring monitoring;

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestUneval");
    }

    @Before
	public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableUnevalizer();
		Options.get().enableContextSensitiveHeap();
		Options.get().enableParameterSensitivity();
        monitoring = CompositeMonitoring.buildFromList(new Monitoring(), new OrdinaryExitReachableCheckerMonitor());
    }

	@Test
	public void uneval_00() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval00.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_01() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval01.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_02() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval02.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_03() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval03.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_04() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval04.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_05() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval05.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}

	@Test
	public void uneval_06() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval06.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}

    @Test(expected = AnalysisLimitationException.class /* aliased eval */)
    public void uneval_07() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval07.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_08() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval08.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_09() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval09.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}

    @Test(expected = AnalysisLimitationException.class /* last evaluated expression as eval result */)
    public void uneval_10() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval10.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_11() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval11.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_12() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval12.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}

    @Test(expected = AnalysisException.class /* GitHub #248 */)
    public void uneval_13() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval13.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}

	@Test
	public void uneval_14() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.get().enableIncludeDom();
		String[] args = {"test/uneval/uneval14.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
		Misc.checkSystemOutput();
	}

	@Test
	public void uneval_15() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.get().enableIncludeDom();
		String[] args = {"test/uneval/uneval15.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_16() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.get().enableIncludeDom();
		String[] args = {"test/uneval/uneval16.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_17() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval17.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_18() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval18.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_19() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.get().enableIncludeDom();
		String[] args = {"test/uneval/uneval19.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
		Misc.checkSystemOutput();
	}

    @Test
    public void uneval_20() throws Exception {
		Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval20.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
	}

    @Test
    public void uneval_21() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval21.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_22() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval22.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_23() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval23.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_24() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval24.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_25() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval25.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisException.class /* GitHub #248 */)
    public void uneval_26() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval26.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_27() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval27.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_27un() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval27.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }


    @Test
    public void uneval_28() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval28.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_29() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval29.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_30() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval30.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_31() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval31.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_32() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval32.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_33() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval33.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_34() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval34.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_35() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval35.html"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_36() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval36.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_37() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval37.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_38() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval38.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_39() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval39.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_40() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval40.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_41() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval41.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_42() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval42.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_43() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval43.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_44() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval44.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_45() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval45.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_46() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval46.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class /* aliased eval */)
    public void uneval_47() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval47.js"};
        Misc.run(args, monitoring);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_48() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval48.js"};
        Misc.run(args, monitoring);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_setIntervalVariants() throws Exception {
        Misc.init();
        //Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval_setIntervalVariants.js"};
        Misc.run(args, monitoring);
        //Misc.checkSystemOutput();
    }

    @Test
    public void uneval_stackOverflowRegression() throws Exception {
        // should not crash
        Misc.init();
        Misc.runSource(new String[]{"function f(arg) {",
                " for (var p in this){}",
                " eval(arg);",
                "}",
                "f(42);",
                ""}, monitoring);
    }

    @Test
    public void uneval_undefinedRegisterRegression() throws Exception {
        // should not crash
        Misc.init();
        Misc.runSource(
                new String[]{"eval('Object();var t=Number();');"},
                monitoring
        );
    }

    @Test
    public void uneval_indexOutOfBoundsRegression() throws Exception {
        // should not crash
        Options.get().enableIncludeDom();
        Misc.init();
        Misc.runSource(
                new String[]{"setInterval(42);"},
                monitoring
        );
    }

    @Test
    public void uneval_declareFunctionRegression() throws Exception {
        Misc.init();
        Misc.runSource(
                new String[]{"eval('function f(){}'); TAJS_assert(typeof f === 'function');"},
                monitoring);
    }

    @Test
    public void uneval_declareNestedFunctionRegression() throws Exception {
        Misc.init();
        Misc.runSource(
                new String[]{"function f (){eval('function g(){}'); TAJS_assert(typeof g === 'function');} f();"},
                monitoring);
    }

    @Test
    public void uneval_newFunction() throws Exception {
        Misc.init();
        Misc.runSource(
                new String[]{"new Function()"},
                monitoring);
    }

    @Test(expected = AnalysisLimitationException.class /* attempting to parse 'TAJS_TEMPORARY_11;}'*/)
    public void uneval_syntaxParts() throws Exception {
        Misc.init();
        Misc.runSource(
                new String[]{
                        "var count;",
                        "function f () {",
                        "  var code = count++ + ' = function(){return ';",
                        "  eval(code.substring() + ';}');",
                        "};",
                        "f();",
                        "f();"
                },
                monitoring);
    }
}
