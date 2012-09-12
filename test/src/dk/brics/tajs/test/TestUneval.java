package dk.brics.tajs.test;

import org.junit.Before;
import org.junit.Test;

import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisException;
//import org.junit.Ignore;

@SuppressWarnings("static-method")
public class TestUneval {
	
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestUneval");
    }

    @Before
	public void init() {
        Options.reset();
        Options.enableTest();
        Options.enableUnevalMode();
	}
	
	@Test
	public void tesUneval00() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval00.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval01() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval01.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval02() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval02.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval03() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval03.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval04() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval04.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval05() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval05.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

	@Test
	public void tesUneval06() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval06.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test(expected=AnalysisException.class)
	public void tesUneval07() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval07.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval08() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval08.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval09() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval09.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test(expected=AnalysisException.class)
	public void tesUneval10() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval10.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval11() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval11.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval12() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval12.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval13() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval13.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval14() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableIncludeDom();
		String[] args = {"test/uneval/uneval14.html"};
		Misc.run(args);
		Options.disableIncludeDom();
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval15() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableIncludeDom();
		String[] args = {"test/uneval/uneval15.html"};
		Misc.run(args);
		Options.disableIncludeDom();
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval16() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableIncludeDom();
		String[] args = {"test/uneval/uneval16.html"};
		Misc.run(args);
		Options.disableIncludeDom();
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval17() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval17.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval18() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval18.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval19() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		Options.enableIncludeDom();
		String[] args = {"test/uneval/uneval19.html"};
		Misc.run(args);
		Options.disableIncludeDom();
		Misc.checkSystemOutput();
	}
	
	@Test
	public void tesUneval20() throws Exception {
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/uneval/uneval20.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}

    @Test
    public void tesUneval21() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval21.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval22() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval22.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval23() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval23.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval24() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval24.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval25() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval25.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test(expected=AnalysisException.class)
    public void tesUneval26() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval26.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval27() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval27.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval27un() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        Options.enableUnrollOneAndAHalf();
        String[] args = {"test/uneval/uneval27.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Options.disableUnrollOneAndAHalf();
        Misc.checkSystemOutput();
    }


    @Test
    public void tesUneval28() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval28.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval29() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval29.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval30() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval30.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval30un() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        Options.enableUnrollOneAndAHalf();
        String[] args = {"test/uneval/uneval30.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Options.disableUnrollOneAndAHalf();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval31() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval31.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval32() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval32.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval33() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval33.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval34() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval34.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval35() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval35.html"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval36() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval36.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval37() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval37.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval38() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval38.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval39() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval39.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval40() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval40.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval41() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval41.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval42() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval42.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval43() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval43.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval44() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval44.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval45() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval45.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval46() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval46.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test(expected=AnalysisException.class)
    public void tesUneval47() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = {"test/uneval/uneval47.js"};
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void tesUneval48() throws Exception { // FIXME: 'this' is incorrect for setInterval code (see FIXME in FunctionCalls.EventHandlerCall)
        Misc.init();
        Misc.captureSystemOutput();
        Options.enableIncludeDom();
        String[] args = {"test/uneval/uneval48.js"};
        Misc.run(args);
        Options.disableIncludeDom();
        Misc.checkSystemOutput();
    }
}
