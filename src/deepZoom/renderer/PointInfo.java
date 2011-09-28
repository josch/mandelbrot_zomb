package deepZoom.renderer;

import digisoft.custom.util.geom.DoubleDouble;

/**
 * @author Zom-B
 * @since 1.0
 * @date May 2, 2009
 */
public class PointInfo {

    public DoubleDouble px;
    public DoubleDouble py;
    public boolean inside;
    public long lastIter;
    public DoubleDouble lastZX = new DoubleDouble();
    public DoubleDouble lastZY = new DoubleDouble();
    protected int antialiasFactor;
    protected int antialiasReach;
    protected int antialiasArea;
}
