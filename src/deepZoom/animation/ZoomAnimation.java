package deepZoom.animation;

import digisoft.custom.util.geom.DoubleDouble;

/**
 * Animation that just zooms. The center parameters and bailout are not
 * animated, and the zooming position is the center of the fractal. ie. set the
 * center of the fractal to the center of thezoomed frame. The maxiter is also
 * animated to account for constant detail with maximum speed throughout the
 * animation. For this, the width of the frame in pixels is also required.
 * Bailout is also not animated.
 * 
 * @author Zom-B
 * @since 1.0
 * @date May 2, 2009
 */
public class ZoomAnimation extends Animation {

    private int width;
    private double magnStart;
    private double magnEnd;
    private double factorMagn;

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setNumFrames(int numFrames) {
        this.numFrames = numFrames;
    }

    @Override
    public void setCenterX(DoubleDouble centerX) {
        this.centerX = centerX;
    }

    @Override
    public void setCenterY(DoubleDouble centerY) {
        this.centerY = centerY;
    }

    public void setMagnStart(double magnStart) {
        this.magnStart = magnStart;
    }

    public void setMagnEnd(double magnEnd) {
        this.magnEnd = magnEnd;
    }

    @Override
    public void setBailout(double bailout) {
        this.bailout = bailout;
    }

    @Override
    public void init() {
        factorMagn = StrictMath.pow(magnEnd / magnStart, 1.0 / (numFrames - 1));
    }

    @Override
    public void setFrame(int frameNr) {
        magn = magnStart * StrictMath.pow(factorMagn, frameNr - 1);

        maxiter = (long) (width * StrictMath.pow(magn, -0.5));

        System.out.println(magn);
    }
}
