package deepZoom.colorings;

import deepZoom.renderer.PointInfo;

import digisoft.custom.math.NumberConstants;
import digisoft.custom.swing.gradient.Gradient;
import digisoft.custom.util.geom.DoubleDouble;

/**
 * @author Zom-B
 * @since 1.0
 * @date May 1, 2009
 */
public class SmoothIterationsColoringBad extends GradientColoring {

    private double logLogBailout;

    public SmoothIterationsColoringBad(Gradient gradient) {
        super(gradient);
    }

    @Override
    public void initParameters() {
        logLogBailout = StrictMath.log(StrictMath.log(parameters.getBailout()));
    }

    @Override
    public float getIndex(PointInfo resultSet) {
        long i = resultSet.lastIter;
        DoubleDouble zx = resultSet.lastZX;
        DoubleDouble zy = resultSet.lastZY;

        double r = StrictMath.sqrt(zx.hi * zx.hi + zy.hi * zy.hi);
        double c = i - 1.28 + (logLogBailout - StrictMath.log(StrictMath.log(r))) * NumberConstants.Q1LOG2;
        return (float) (StrictMath.log(c / 64 + 1) / NumberConstants.LOG2 + 0.45);
    }
}
