package deepZoom;

import java.io.IOException;

import deepZoom.colorings.Coloring;
import deepZoom.colorings.SmoothIterationsColoringBad;
import deepZoom.fractals.Fractal;
import deepZoom.fractals.Mandel;
import deepZoom.fractals.MandelPrecision;
import deepZoom.parameters.ZoomAnimation;
import deepZoom.renderer.Scene;
import deepZoom.viewports.Viewport;

import digisoft.custom.NumberFunctions;
import digisoft.custom.awt.Color3f;
import digisoft.custom.swing.gradient.Gradient;
import digisoft.custom.swing.gradient.OpaqueGradient;
import digisoft.custom.util.geom.DoubleDouble;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * @author Zom-B
 * @since 1.0
 * @see date 2006/10/20
 */
public class MandelAnimator {

    private static final String SAVEPATH = "./";
    private static final int WIDTH = (int) (1920);
    private static final int HEIGHT = (int) (1080);
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
    private Viewport viewport = new Viewport();
    private ZoomAnimation animation = new ZoomAnimation();
    private Fractal fractal = new Mandel();
    private Coloring coloring = new SmoothIterationsColoringBad(makeGradient());
    private Scene scene = new Scene();
    private int frameNr;
    private boolean antialiasStep = false;
    private long runTime;
    private long frameTime;

    private int[] fractal_pixels;
    private int[] iter_pixels;
    private int[] aa_pixels;

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

        fractal_pixels = new int[MandelAnimator.WIDTH*MandelAnimator.HEIGHT];
        iter_pixels = new int[MandelAnimator.WIDTH*MandelAnimator.HEIGHT];
        aa_pixels = new int[MandelAnimator.WIDTH*MandelAnimator.HEIGHT];

        scene.setViewport(viewport);
        scene.setColorPixels(fractal_pixels);
        scene.setIterMap(iter_pixels);
        scene.setEdgeMap(aa_pixels);
        scene.setNumCPUs(MandelAnimator.NUM_CPUS);
        scene.setFractal(fractal);
        scene.setColoring(coloring);

        frameNr = MandelAnimator.FRAME_START;

        runTime = frameTime = System.currentTimeMillis();
        while (true) {
            prepareCalculation();
            calculate(0);
            calculationCompleted();
        }
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
            BufferedImage saveImage = new BufferedImage(MandelAnimator.WIDTH, MandelAnimator.HEIGHT, BufferedImage.TYPE_INT_RGB);
            saveImage.setRGB(0, 0, MandelAnimator.WIDTH, MandelAnimator.HEIGHT, fractal_pixels, 0, MandelAnimator.WIDTH);

            OutputStream out = new BufferedOutputStream(new FileOutputStream(MandelAnimator.SAVEPATH + NumberFunctions.toStringFixedLength(frameNr, 4) + ".png"));
            ImageIO.write(saveImage, "png", out);

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void destroyImage() {
        System.gc();
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

    public void prepareCalculation() {
        if (!antialiasStep) {
            animation.setFrame(frameNr);

            System.out.println("Rough: " + frameNr + " magn=" + animation.getMagn() + " maxiter=" + animation.getMaxiter());

            destroyImage();
            scene.initFrame();
        } else {
            animation.setFrame(frameNr);
            animation.setMaxiter(animation.getMaxiter() << 2);

            System.out.println("AA:    " + frameNr + " magn=" + animation.getMagn() + " maxiter=" + animation.getMaxiter());

            scene.calcAntialiasMask();
        }
    }

    public void calculate(int cpu) {
        if (!antialiasStep) {
            scene.render(cpu);
        } else {
            scene.renderAntialias(cpu);
        }
    }

    public void calculationCompleted() {
        if (!antialiasStep && MandelAnimator.DO_ANTIALIAS) {
            antialiasStep = true;
        } else {
            antialiasStep = false;

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
}
