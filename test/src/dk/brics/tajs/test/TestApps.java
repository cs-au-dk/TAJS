package dk.brics.tajs.test;

import org.junit.Before;
import org.junit.Test;

import dk.brics.tajs.options.Options;

@SuppressWarnings("static-method")
public class TestApps { 

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.TestApps");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.enableTest();
        Options.enablePolymorphic();
	}
	
	@Test
	public void apps_jscrypto_encrypt_cookie() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/jscrypto/encrypt_cookie.html", "test/apps/jscrypto/jscrypto.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void apps_jscrypto_encrypt_from_form() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/jscrypto/encrypt_from_form.html", "test/apps/jscrypto/jscrypto.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void apps_mceditor_simple() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/mceditor/simple.html", "test/apps/mceditor/tiny_mce_src.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void apps_mceditor_full() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/mceditor/full.html", "test/apps/mceditor/tiny_mce_src.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void apps_minesweeper() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/minesweeper/minesweeper.html"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void apps_paint() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/paint/index.html", "test/apps/paint/paint.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void apps_projavascript_clock() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/projavascript/clock/07-clock.html"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void apps_projavascript_gallery() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/projavascript/gallery/index.html", "test/apps/projavascript/gallery/library.js", "test/apps/projavascript/gallery/gallery.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void apps_projavascript_sun() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/projavascript/sun/08-sun.html"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void apps_samegame() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/samegame/index.html", "test/apps/samegame/header.js", "test/apps/samegame/main.js"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void apps_simplecalc() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/simplecalc/math2.html"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
	
	@Test
	public void apps_solitaire() throws Exception { 
		Misc.init();
		Misc.captureSystemOutput();
		String[] args = {"test/apps/solitaire/spider.html"};
		Misc.run(args);
		Misc.checkSystemOutput();
	}
}
