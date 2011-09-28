package deepZoom.colorings;

import deepZoom.parameters.Parameters;
import deepZoom.renderer.PointInfo;

/**
 * @author Zom-B
 * @since 1.0
 * @date May 1, 2009
 */
public abstract class Coloring {

    protected Parameters parameters;

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public abstract void initParameters();

    public abstract int getColor(PointInfo resultSet);
}
