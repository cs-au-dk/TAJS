package dk.brics.tajs.test;

// import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.brics.tajs.options.Options;

@SuppressWarnings("static-method")
public class Test10K {

	public static void main(String[] args) {
		org.junit.runner.JUnitCore.main("dk.brics.tajs.test.Test10K");
	}
	
	@Before
	public void init() {
        Options.reset();
		Options.get().enableTest();
        Options.get().enableIncludeDom();
	}

    @Test
    public void test10k_10k_snake() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/10k_snake.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_10k_world() throws Exception {
        Misc.init();
        Options.get().enableUnevalizer();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/10k_world.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_3d_maker() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/3d_maker.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_attractor() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/attractor.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_canvas_aquarium() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/canvas_aquarium.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_defend_yourself() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/defend_yourself.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_earth_night_lights() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/earth_night_lights.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_filterrific() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/filterrific.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_flatwar() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/flatwar.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_floating_bubbles() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/floating_bubbles.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_fractal_landscape() throws Exception {
        Misc.init();
        Options.get().enableUnevalizer();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/fractal_landscape.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_gravity() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/gravity.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_heatmap() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/heatmap.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_last_man_standing() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/last_man_standing.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_lines() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/lines.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_minesweeper() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/minesweeper.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Ignore // See GitHub #147
    @Test
    public void test10k_nbody() throws Exception {
        Misc.init();
        Options.get().enableUnevalizer();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/nbody.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_rgb_color_wheel() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/rgb_color_wheel.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_sinuous() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/sinuous.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_snowpar() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/snowpar.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_stairs_to_heaven() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/stairs_to_heaven.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_sudoku() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/sudoku.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_tictactoe() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/tictactoe.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_zmeyko() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/10k/zmeyko.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }
}
