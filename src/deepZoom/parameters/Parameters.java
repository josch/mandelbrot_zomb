package deepZoom.parameters;

import digisoft.custom.util.geom.DoubleDouble;

/**
 * @author Zom-B
 * @since 1.0
 * @date May 2, 2009
 */
public class Parameters {

    protected DoubleDouble centerX = new DoubleDouble();
    protected DoubleDouble centerY = new DoubleDouble();
    protected double magn;
    protected long maxiter;
    protected double bailout;

    public void setCenterX(DoubleDouble centerX) {
        this.centerX = centerX;
    }

    public DoubleDouble getCenterX() {
        return centerX;
    }

    public void setCenterY(DoubleDouble centerY) {
        this.centerY = centerY;
    }

    public DoubleDouble getCenterY() {
        return centerY;
    }

    public void setMagn(double magn) {
        this.magn = magn;
    }

    public double getMagn() {
        return magn;
    }

    public void setMaxiter(long maxiter) {
        this.maxiter = maxiter;
    }

    public long getMaxiter() {
        return maxiter;
    }

    public void setBailout(double bailout) {
        this.bailout = bailout;
    }

    public double getBailout() {
        return bailout;
    }
}
