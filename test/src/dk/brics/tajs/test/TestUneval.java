package dk.brics.tajs.test;

import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


@SuppressWarnings("static-method")
public class TestUneval {
    // XXX see ignored tests

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestUneval");
    }

    @Before
	public void init() {
        Options.reset();
        Options.get().enableTest();
        Options.get().enableUnevalizer();
		Options.get().enableContextSensitiveHeap();
		Options.get().enableParameterSensitivity();
	}

	@Test
	public void uneval_00() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval00.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_01() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval01.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_02() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval02.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_03() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval03.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_04() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval04.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_05() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval05.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void uneval_06() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval06.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Ignore // see GitHub issue #146
    @Test
	public void uneval_07() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval07.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_08() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval08.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_09() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval09.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Ignore // see GitHub issue #146
    @Test
	public void uneval_10() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval10.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_11() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval11.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_12() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval12.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Ignore // FIXME nested evals does not set BasicBlock.entry_block!?
	@Test
	public void uneval_13() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval13.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void uneval_14() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.get().enableIncludeDom();
		String[] args = {"test/uneval/uneval14.html"};
		Misc.run(args);
		Options.get().disableIncludeDom();
		Misc.checkSystemOutput();
	}

	@Test
	public void uneval_15() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.get().enableIncludeDom();
		String[] args = {"test/uneval/uneval15.html"};
		Misc.run(args);
		Options.get().disableIncludeDom();
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_16() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.get().enableIncludeDom();
		String[] args = {"test/uneval/uneval16.html"};
		Misc.run(args);
		Options.get().disableIncludeDom();
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_17() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval17.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_18() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval18.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void uneval_19() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.get().enableIncludeDom();
		String[] args = {"test/uneval/uneval19.html"};
		Misc.run(args);
		Options.get().disableIncludeDom();
		Misc.checkSystemOutput();
	}

    @Ignore // Unsound, see #175
	@Test
	public void uneval_20() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval20.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Test
    public void uneval_21() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval21.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_22() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval22.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_23() throws Exception {
        // XXX fix propagation at dk/brics/tajs/analysis/dom/DOMWindow.java:440
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval23.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_24() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval24.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_25() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval25.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Ignore // TODO FIXME #131
    @Test
    public void uneval_26() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval26.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_27() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval27.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_27un() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        Options.get().enableUnrollOneAndAHalf();
        String[] args = {"test/uneval/uneval27.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Options.get().disableUnrollOneAndAHalf();
        Misc.checkSystemOutput();
    }


    @Test
    public void uneval_28() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval28.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

	@Ignore // FIXME: assignment to innerHTML
    @Test
    public void uneval_29() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval29.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_30() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval30.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_30un() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        Options.get().enableUnrollOneAndAHalf();
        String[] args = {"test/uneval/uneval30.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Options.get().disableUnrollOneAndAHalf();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_31() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval31.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_32() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval32.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_33() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval33.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_34() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval34.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_35() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval35.html"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_36() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval36.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_37() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval37.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_38() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval38.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_39() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval39.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_40() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval40.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_41() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval41.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_42() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval42.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_43() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval43.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_44() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval44.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_45() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval45.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_46() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval46.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Ignore // see GitHub issue #146
    @Test
    public void uneval_47() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval47.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Ignore
    @Test
    public void uneval_48() throws Exception { // FIXME: 'this' is incorrect for setInterval code (see FIXME in FunctionCalls.EventHandlerCall)
        // XXX fix propagation at dk/brics/tajs/analysis/dom/DOMWindow.java:440
        Misc.init();
        Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval48.js"};
        Misc.run(args);
        Options.get().disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void uneval_setIntervalVariants() throws Exception {
        Misc.init();
        //Misc.captureSystemOutput();
        Options.get().enableIncludeDom();
        String[] args = {"test/uneval/uneval_setIntervalVariants.js"};
        Misc.run(args);
        //Misc.checkSystemOutput();
    }
}
