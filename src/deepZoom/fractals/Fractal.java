package deepZoom.fractals;

import deepZoom.parameters.Parameters;
import deepZoom.renderer.PointInfo;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/05/01
 */
public abstract class Fractal {

    protected Parameters parameters;

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public abstract void initFrame();

    /**
     * 
     * @param x
     * @param y
     * @param result
     *            float array with {gradient index, last iter}
     */
    public abstract void calcPoint(PointInfo pointInfo);
}
