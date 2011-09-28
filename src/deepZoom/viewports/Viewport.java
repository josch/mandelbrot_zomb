package deepZoom.viewports;

import deepZoom.parameters.Parameters;
import deepZoom.renderer.PointInfo;

import digisoft.custom.util.geom.DoubleDouble;

/**
 * Mapping from (sub)pixel coordinates to coordinates in the complex plane.
 * Don't forget to call init() after changing the input or output configuration.
 * 
 * @author Zom-B
 * @since 1.0
 * @date 2009/04/19
 */
public class Viewport {
    // Input configuration.

    public int width;
    public int height;
    // Output configuration.
    private Parameters parameters;
    // Internally used values.
    private transient DoubleDouble x0d = new DoubleDouble(0);
    private transient DoubleDouble x2 = new DoubleDouble(0);
    private transient DoubleDouble y1d = new DoubleDouble(0);
    private transient DoubleDouble y2 = new DoubleDouble(0);

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    /** Call each time the viewport or parameters change. */
    public void initParameters() {
        x0d.set(4, 0);
        x0d.divSelf(parameters.getMagn());
        x0d.divSelf(width);

        x2.set(-2 / parameters.getMagn(), 0);
        x2.addSelf(parameters.getCenterX());

        y1d.set(-4, 0);
        y1d.divSelf(parameters.getMagn());
        y1d.divSelf(width);

        y2.set(2 / parameters.getMagn() * height / width, 0);
        y2.addSelf(parameters.getCenterY());
    }

    public DoubleDouble getPX(double x, double y) {
        DoubleDouble px = new DoubleDouble(x);
        px.mulSelf(x0d);
        px.addSelf(x2);
        return px;
    }

    public DoubleDouble getPY(double x, double y) {
        DoubleDouble py = new DoubleDouble(y);
        py.mulSelf(y1d);
        py.addSelf(y2);
        return py;
    }

    public void getPoint(double x, double y, PointInfo pointInfo) {
        pointInfo.px = getPX(x, y);
        pointInfo.py = getPY(x, y);
    }
}
