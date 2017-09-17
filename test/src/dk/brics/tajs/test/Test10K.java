package dk.brics.tajs.test;
// import static org.junit.Assert.fail;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class Test10K {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.Test10K");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableIncludeDom();
    }

    @Test
    public void test10k_10k_snake() throws Exception {
        Options.get().enablePolyfillMDN();
        Misc.run("test-resources/src/10k/10k_snake.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_10k_world() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/10k/10k_world.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_3d_maker() throws Exception {
        Misc.run("test-resources/src/10k/3d_maker.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_attractor() throws Exception {
        Misc.run("test-resources/src/10k/attractor.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_canvas_aquarium() throws Exception {
        Misc.run("test-resources/src/10k/canvas_aquarium.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_defend_yourself() throws Exception {
        Misc.run("test-resources/src/10k/defend_yourself.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_earth_night_lights() throws Exception {
        Misc.run("test-resources/src/10k/earth_night_lights.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_filterrific() throws Exception {
        Misc.run("test-resources/src/10k/filterrific.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_flatwar() throws Exception {
        Misc.run("test-resources/src/10k/flatwar.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_floating_bubbles() throws Exception {
        Options.get().enablePolyfillMDN();
        Misc.run("test-resources/src/10k/floating_bubbles.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_fractal_landscape() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/10k/fractal_landscape.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_gravity() throws Exception {
        Misc.run("test-resources/src/10k/gravity.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_heatmap() throws Exception {
        Misc.run("test-resources/src/10k/heatmap.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_last_man_standing() throws Exception {
        Misc.run("test-resources/src/10k/last_man_standing.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_lines() throws Exception {
        Misc.run("test-resources/src/10k/lines.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_minesweeper() throws Exception {
        Misc.run("test-resources/src/10k/minesweeper.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_nbody() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/10k/nbody.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_rgb_color_wheel() throws Exception {
        Misc.run("test-resources/src/10k/rgb_color_wheel.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_sinuous() throws Exception {
        Misc.run("test-resources/src/10k/sinuous.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_snowpar() throws Exception {
        Misc.run("test-resources/src/10k/snowpar.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_stairs_to_heaven() throws Exception {
        Misc.run("test-resources/src/10k/stairs_to_heaven.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_sudoku() throws Exception {
        Misc.run("test-resources/src/10k/sudoku.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_tictactoe() throws Exception {
        Misc.run("test-resources/src/10k/tictactoe.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void test10k_zmeyko() throws Exception {
        Misc.run("test-resources/src/10k/zmeyko.html");
        Misc.checkSystemOutput();
    }
}
