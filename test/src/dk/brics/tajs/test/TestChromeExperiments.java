package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.options.Options;
import dk.brics.tajs.util.AnalysisLimitationException;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("static-method")
public class TestChromeExperiments {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("dk.brics.tajs.test.ChromeExperiments");
    }

    @Before
    public void init() {
        Main.reset();
        Options.get().enableTest();
        Options.get().enableIncludeDom();
        Options.get().enableUnevalizer();
    }

    @Test
    public void chrome_3ddemo() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/3ddemo.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_anotherworld() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/anotherworld.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_apophis() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/apophis.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_aquarium() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/aquarium.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_bingbong() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/bingbong.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_blob() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/blob.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_bomomo() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/chromeexperiments/bomomo.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_breathingGalaxies() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/breathinggalaxies.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_browserball() throws Exception {
        Options.get().enablePolyfillMDN();
        Misc.run("test-resources/src/chromeexperiments/browserball.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_burncanvas() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/burncanvas.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_catchit() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/chromeexperiments/catchit.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_core() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/core.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_deepseastress() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/deepseastress.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_harmony() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/harmony.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_jstouch() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/jstouch.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_kaleidoscope() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/chromeexperiments/kaleidoscope.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_keylight() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/keylight.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_liquidparticles() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/liquidparticles.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_magnetic() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/magnetic.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_orangetunnel() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/orangetunnel.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_planedeformations() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/planedeformations.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_plasma() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/plasma.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_raytracer() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/chromeexperiments/raytracer.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_sandtrap() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/sandtrap.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_starfield() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/starfield.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_strangeattractor() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/strangeattractor.html");
        Misc.checkSystemOutput();
    }

    @Test(expected = AnalysisLimitationException.class /* impossible eval from url content */)
    public void chrome_tetris() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/chromeexperiments/tetris.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_trail() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/trail.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_tunneler() throws Exception {
        Options.get().enableUnevalizer();
        Misc.run("test-resources/src/chromeexperiments/tunneler.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_voronoi() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/voronoi.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_voxels() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/voxels.html");
        Misc.checkSystemOutput();
    }

    @Test
    public void chrome_watertype() throws Exception {
        Misc.run("test-resources/src/chromeexperiments/watertype.html");
        Misc.checkSystemOutput();
    }
}
