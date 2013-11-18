package dk.brics.tajs.test;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.brics.tajs.options.Options;

@SuppressWarnings("static-method")
public class TestChromeExperiments {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.ChromeExperiments");
    }

    @Before
    public void init() {
        Options.reset();
        Options.enableTest();
        Options.enableIncludeDom();
    }

    @Test
    public void chrome_3ddemo() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/3ddemo.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_anotherworld() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/anotherworld.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_apophis() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/apophis.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_aquarium() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/aquarium.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_bingbong() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/bingbong.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_blob() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/blob.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_bomomo() throws Exception {
        Misc.init();
        Options.enableUnevalizer();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/bomomo.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_breathingGalaxies() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/breathinggalaxies.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_browserball() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/browserball.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_burncanvas() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/burncanvas.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_catchit() throws Exception {
        Misc.init();
        Options.enableUnevalizer();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/catchit.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_core() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/core.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_deepseastress() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/deepseastress.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_harmony() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/harmony.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_jstouch_example() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/jstouch_example.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_jstouch() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/jstouch.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_kaleidoscope() throws Exception {
        Misc.init();
        Options.enableUnevalizer();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/kaleidoscope.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_keylight() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/keylight.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_liquidparticles() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/liquidparticles.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_magnetic() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/magnetic.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_orangetunnel() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/orangetunnel.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_planedeformations() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/planedeformations.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_plasma() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/plasma.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_raytracer() throws Exception {
        Misc.init();
        Options.enableUnevalizer();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/raytracer.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_sandtrap() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/sandtrap.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_starfield() throws Exception {
        Misc.init();
        Options.enableUnevalizer();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/starfield.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_strangeattractor() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/strangeattractor.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Ignore
    @Test
    public void chrome_tetris() throws Exception { // FIXME: unevalable eval
        Misc.init();
        Options.enableUnevalizer();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/tetris.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_trail() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/trail.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_tunneler() throws Exception {
        Misc.init();
        Options.enableUnevalizer();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/tunneler.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_voronoi() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/voronoi.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_voxels() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/voxels.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_watertype() throws Exception {
        Misc.init();
        Misc.captureSystemOutput();
        String[] args = { "test/chromeexperiments/watertype.html" };
        Misc.run(args);
        Misc.checkSystemOutput();
    }
}
