package deepZoom.colorings;

import deepZoom.renderer.PointInfo;

import digisoft.custom.swing.gradient.Gradient;

/**
 * @author Zom-B
 * @since 1.0
 * @date May 2, 2009
 */
public abstract class GradientColoring extends Coloring {

    protected Gradient gradient;

    public GradientColoring(Gradient gradient) {
        this.gradient = gradient;
    }

    @Override
    public int getColor(PointInfo resultSet) {
        return gradient.get(getIndex(resultSet) % 1);
    }

    public abstract float getIndex(PointInfo resultSet);
}
