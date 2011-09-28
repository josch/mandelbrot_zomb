package deepZoom;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import deepZoom.calculator.Calculator;
import deepZoom.calculator.ParallelCalculator;
import deepZoom.colorings.Coloring;
import deepZoom.colorings.SmoothIterationsColoringBad;
import deepZoom.fractals.Fractal;
import deepZoom.fractals.Mandel;
import deepZoom.fractals.MandelPrecision;
import deepZoom.parameters.ZoomAnimation;
import deepZoom.renderer.Layer;
import deepZoom.renderer.Scene;
import deepZoom.schedulers.AngularScheduler;
import deepZoom.schedulers.CRTScheduler;
import deepZoom.schedulers.ClockScheduler;
import deepZoom.schedulers.DitherScheduler;
import deepZoom.schedulers.FlowerScheduler;
import deepZoom.schedulers.ModScheduler;
import deepZoom.schedulers.PythagorasScheduler;
import deepZoom.schedulers.RadialScheduler;
import deepZoom.schedulers.RandomScheduler;
import deepZoom.schedulers.Scheduler;
import deepZoom.schedulers.SimpleScheduler;
import deepZoom.schedulers.SpiralScheduler;
import deepZoom.schedulers.SplitScheduler;
import deepZoom.schedulers.SquareSpiralScheduler;
import deepZoom.schedulers.XorScheduler;
import deepZoom.viewports.Viewport;

import digisoft.custom.NumberFunctions;
import digisoft.custom.awt.Color3f;
import digisoft.custom.swing.ImageFunctions;
import digisoft.custom.swing.RefreshListener;
import digisoft.custom.swing.RefreshThread;
import digisoft.custom.swing.gradient.Gradient;
import digisoft.custom.swing.gradient.OpaqueGradient;
import digisoft.custom.swing.window.PixelWindow;
import digisoft.custom.util.geom.DoubleDouble;
import java.io.File;

/**
 * @author Zom-B
 * @since 1.0
 * @see date 2006/10/20
 */
public class MandelAnimator implements RefreshListener, Calculator, MouseListener {

    private static final String SAVEPATH = "C:/Animation golden/";
    private static final int WIDTH = (int) (1024);
    private static final int HEIGHT = (int) (576);
    private static int FRAME_START = 1;
    private static final int FRAME_END = 3600;
    private static final int FRAME_STEP = 1;
    private static final boolean DO_ANTIALIAS = true;
    //Traditional Wikipedia        
    private DoubleDouble centerX = new DoubleDouble(-0.7436438870371587, -3.628952515063387E-17);
    private DoubleDouble centerY = new DoubleDouble(0.13182590420531198, -1.2892807754956678E-17);
    //Line and cross   
    //private DoubleDouble centerX = new DoubleDouble(-0.743646040973709, -4.790419761500457E-18);
    //private DoubleDouble centerY = new DoubleDouble(0.13182426946026354, 1.9785505600065397E-18);
    private static final double MIN_ZOOM = 1;
    private static final double MAX_ZOOM = 1e30;
    private static final int NUM_FRAMES = 3600;

    public static void main(String[] args) {
        new MandelAnimator();
    }
    private static final int NUM_CPUS = Runtime.getRuntime().availableProcessors();
    private PixelWindow aaWindow = new PixelWindow(10, 10, MandelAnimator.WIDTH, MandelAnimator.HEIGHT);
    private PixelWindow iterWindow = new PixelWindow(10, 10, MandelAnimator.WIDTH, MandelAnimator.HEIGHT);
    private PixelWindow fractalWindow = new PixelWindow(10, 10, MandelAnimator.WIDTH, MandelAnimator.HEIGHT);
    private Viewport viewport = new Viewport();
    private Layer layer = new Layer();
    private ZoomAnimation animation = new ZoomAnimation();
    private Fractal fractal = new Mandel();
    private Coloring coloring = new SmoothIterationsColoringBad(makeGradient());
    private Scheduler[] schedulers = {new SpiralScheduler(10)};
    private int scheduler;
    private Scene scene = new Scene();
    private int frameNr;
    private boolean antialiasStep = false;
    private long runTime;
    private long frameTime;

    public MandelAnimator() {
        File dirFile = new File(SAVEPATH);
        dirFile.mkdir();

        int newSave = 0;
        for (int i = MandelAnimator.FRAME_START; i <= MandelAnimator.FRAME_END; i++) {
            File test = new File(MandelAnimator.SAVEPATH
                    + NumberFunctions.toStringFixedLength(i, 4) + ".png");
            if (!test.isFile()) {
                newSave = i;
                break;
            }
        }
        MandelAnimator.FRAME_START = newSave;

        fractalWindow.addMouseListener(this);
        iterWindow.setTitle("Iter");
        aaWindow.setTitle("AA");

        animation.setWidth(MandelAnimator.WIDTH);
        animation.setNumFrames(MandelAnimator.NUM_FRAMES);
        animation.setCenterX(centerX);
        animation.setCenterY(centerY);
        animation.setMagnStart(MandelAnimator.MIN_ZOOM);
        animation.setMagnEnd(MandelAnimator.MAX_ZOOM);
        animation.setBailout(128);
        animation.init();
        viewport.setSize(MandelAnimator.WIDTH, MandelAnimator.HEIGHT);
        viewport.setParameters(animation);
        fractal.setParameters(animation);
        coloring.setParameters(animation);

        for (Scheduler s : schedulers) {
            s.setViewport(viewport);
        }

        scene.setViewport(viewport);
        scene.setColorPixels(fractalWindow.pixels);
        scene.setIterMap(iterWindow.pixels);
        scene.setEdgeMap(aaWindow.pixels);
        scene.setNumCPUs(MandelAnimator.NUM_CPUS);

        {
            layer.setFractal(fractal);
            layer.setColoring(coloring);
            scene.addLayer(layer);
        }

        frameNr = MandelAnimator.FRAME_START;

        runTime = frameTime = System.currentTimeMillis();
        ParallelCalculator.createCalculators(this, MandelAnimator.NUM_CPUS);
        new RefreshThread(this, 30).start();
    }

    public static Gradient makeGradient() {
        final Color3f[] c = { 
            new Color3f(14, 11, 82), 
            new Color3f(14, 60, 165), 
            new Color3f(78, 154, 220),
            new Color3f(176, 229, 255),
            new Color3f(249, 253, 239), 
            new Color3f(249, 229, 96), 
            new Color3f(249, 181, 15), 
            new Color3f(217, 108, 3), 
            new Color3f(134, 32, 13), 
            new Color3f(57, 4, 35), 
            new Color3f(14, 11, 82)};
        final float[] f = new float[c.length];
        for (int i = 0; i < c.length; i++) {
            f[i] = i / (float) (c.length - 1);
            f[i] = (float) StrictMath.sqrt(f[i]);
        }

        return new OpaqueGradient(f, c);
    }
    
    /*
    public static Gradient makeGradient() {
        final Color3f[] c = {new Color3f(0, 0, 0f), //
            new Color3f(1, 0, 0f), //
            new Color3f(1, 1, 1f), // Red
            new Color3f(1, 0, 0f), //
            new Color3f(0, 0, 0f), //
            new Color3f(1, 0.4f, 0f), //
            new Color3f(1, 1, 1f), // Orange
            new Color3f(1, 0.4f, 0f), //
            new Color3f(0, 0, 0f), //
            new Color3f(1, 1, 0f), //
            new Color3f(1, 1, 1f), // Yellow
            new Color3f(1, 1, 0f), //
            new Color3f(0, 0, 0f), //
            new Color3f(0, 1, 0f), //
            new Color3f(1, 1, 1f), // Green
            new Color3f(0, 1, 0f), //
            new Color3f(0, 0, 0f), //
            new Color3f(0, 1, 1f), //
            new Color3f(1, 1, 1f), // Cyan
            new Color3f(0, 1, 1f), //
            new Color3f(0, 0, 0f), //
            new Color3f(0, 0, 1f), //
            new Color3f(1, 1, 1f), // Blue
            new Color3f(0, 0, 1f), //
            new Color3f(0, 0, 0f)};
        final float[] f = new float[c.length];
        for (int i = 0; i < c.length; i++) {
            f[i] = i / (float) (c.length - 1);
            f[i] = (float) StrictMath.sqrt(f[i]);
        }

        return new OpaqueGradient(f, c);
    } */

    private void saveFrame() {
        try {
            ImageFunctions.savePNG(MandelAnimator.SAVEPATH + NumberFunctions.toStringFixedLength(frameNr, 4) + ".png", fractalWindow.pixels,
                    MandelAnimator.WIDTH, MandelAnimator.HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void destroyImage() {
        System.gc();
        fractalWindow.clear(0);
        iterWindow.clear(0);
        aaWindow.clear(0);
    }

    private void nextFrame() {
        frameNr += MandelAnimator.FRAME_STEP;
        if (frameNr > MandelAnimator.FRAME_END) {
            // System.exit(0);
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void prepareCalculation() {
        scheduler = NumberFunctions.RND.nextInt(schedulers.length);
        scene.setScheduler(schedulers[scheduler]);

        if (!antialiasStep) {
            fractalWindow.setTitle(Integer.toString(frameNr) + " First pass");

            animation.setFrame(frameNr);

            System.out.println("Rough: " + frameNr + " magn=" + animation.getMagn() + " maxiter=" + animation.getMaxiter());

            destroyImage();
            scene.initFrame();
        } else {
            fractalWindow.setTitle(Integer.toString(frameNr) + " Antialiasing");

            animation.setFrame(frameNr);
            animation.setMaxiter(animation.getMaxiter() << 2);

            System.out.println("AA:    " + frameNr + " magn=" + animation.getMagn() + " maxiter=" + animation.getMaxiter());

            scene.calcAntialiasMask();
        }

        schedulers[scheduler].init();
    }

    @Override
    public void calculate(int cpu) {
        if (!antialiasStep) {
            scene.render(cpu);
        } else {
            scene.renderAntialias(cpu);
        }
    }

    @Override
    public void calculationCompleted() {
        if (!antialiasStep && MandelAnimator.DO_ANTIALIAS) {
            antialiasStep = true;
        } else {
            antialiasStep = false;

            fractalWindow.repaintNow();
            iterWindow.repaintNow();
            saveFrame();

            long time = System.currentTimeMillis();
            System.out.println("Time elapsed: " + (time - runTime) / 1e3 + " / " + (time - frameTime) / 1e3);
            if (time - frameTime < 2000) {
                try {
                    Thread.sleep(2000 - time + frameTime);
                } catch (InterruptedException e) {
                }
            }
            frameTime = System.currentTimeMillis();

            nextFrame();
        }
    }

    @Override
    public void refreshing() {
        fractalWindow.repaintNow();
        iterWindow.repaintNow();
        aaWindow.repaintNow();
        if (antialiasStep) {
            aaWindow.setTitle("AA " + (int) (schedulers[scheduler].getProgress() * 1000) / 10f + "%");
        } else {
            iterWindow.setTitle("Iter " + (int) (schedulers[scheduler].getProgress() * 1000) / 10f + "%");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
            centerX = viewport.getPX(e.getX(), e.getY());
            centerY = viewport.getPY(e.getX(), e.getY());

            System.out.println("/tprivate DoubleDouble/t/t/tcenterX/t/t/t= new DoubleDouble(" + centerX.hi + ", " + centerX.lo + ");");
            System.out.println("/tprivate DoubleDouble/t/t/tcenterY/t/t/t= new DoubleDouble(" + centerY.hi + ", " + centerY.lo + ");");
        } else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
