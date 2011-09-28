package deepZoom.renderer;

import deepZoom.colorings.Coloring;
import deepZoom.fractals.Fractal;

/**
 * @author Zom-B
 * @since 1.0
 * @date May 3, 2009
 */
public class Layer {

    protected Fractal fractal;
    protected Coloring coloring;

    public void setFractal(Fractal fractal) {
        this.fractal = fractal;
    }

    public void setColoring(Coloring coloring) {
        this.coloring = coloring;
    }

    public void initFrame() {
        fractal.initFrame();
        coloring.initParameters();
    }
}
