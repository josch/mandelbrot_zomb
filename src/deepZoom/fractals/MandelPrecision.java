package deepZoom.fractals;

import deepZoom.renderer.PointInfo;

import digisoft.custom.util.geom.DoubleDouble;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/07/09
 */
public class MandelPrecision extends Fractal {

    private static final double PRIODICITY_EPS = 1e-17;

    @Override
    public void initFrame() {
    }

    @Override
    public void calcPoint(PointInfo pointInfo) {
        DoubleDouble zx = new DoubleDouble();
        DoubleDouble zy = new DoubleDouble();
        DoubleDouble xx = new DoubleDouble();
        DoubleDouble yy = new DoubleDouble();
        DoubleDouble temp = new DoubleDouble();

        double bailout = parameters.getBailout();
        long maxiter = parameters.getMaxiter();

        // Calculate viewport.
        // DoubleDouble px = this.viewport.getPX(x, y);
        // DoubleDouble py = this.viewport.getPY(x, y);
        DoubleDouble px = pointInfo.px;
        DoubleDouble py = pointInfo.py;

        // Main bulb check.
        // zx = 4 * (px * px + py * py);
        zy.set(py);
        zy.sqrSelf();
        zx.set(px);
        zx.sqrSelf();
        zx.addSelf(zy);
        zx.mulSelf(4);
        // temp = 2 * px
        xx.set(px);
        xx.mulSelf(2);
        // zy = zx + 4 * temp
        zy.set(xx);
        zy.mulSelf(4);
        zy.addSelf(zx);
        if (zy.hi < -3.75) {
            pointInfo.inside = true;
            pointInfo.lastIter = maxiter;
            pointInfo.lastZX = px;
            pointInfo.lastZY = py;
            return;
        }

        // Cardoid check.
        // zy = zx - temp + 0.25;
        zy.set(zx);
        zy.subSelf(xx);
        zy.addSelf(0.25);
        // temp = (temp + zy - 0.5)**2;
        xx.addSelf(zy);
        xx.addSelf(-0.5);
        xx.sqrSelf();
        // if (zy > temp)
        if (zy.hi > xx.hi) {
            pointInfo.inside = true;
            pointInfo.lastIter = maxiter;
            pointInfo.lastZX = px;
            pointInfo.lastZY = py;
            return;
        }

        zx.set(px);
        zy.set(py);

        // Initial maximum period to detect.
        int check = 3;
        // Maximum period doubles every iterations:
        int whenupdate = 10;
        // Period history registers.
        double hx = 0;
        double hy = 0;

        // long int because maxiter goes above 2**31 bits
        for (long i = 1; i <= 50000; i++) {
            // Precalculate squares.
            xx.set(zx);
            xx.sqrSelf();
            yy.set(zy);
            yy.sqrSelf();

            // Check bailout.
            temp.set(xx);
            temp.addSelf(yy);
            if (temp.hi > bailout) {
                pointInfo.inside = false;
                pointInfo.lastIter = i;
                pointInfo.lastZX = zx;
                pointInfo.lastZY = zy;
                return;
            }

            // Iterate
            // y' = y * x * 2 + cy
            zy.mulSelf(zx);
            zy.addSelf(zy);
            zy.addSelf(py);
            // x' = xx - yy + px
            xx.subSelf(yy);
            zx.set(xx);
            zx.addSelf(px);

            // Periodicity check.
            double d = zx.hi - hx;
            if (d > 0.0 ? d < MandelPrecision.PRIODICITY_EPS : d > -MandelPrecision.PRIODICITY_EPS) {
                d = zy.hi - hy;
                if (d > 0.0 ? d < MandelPrecision.PRIODICITY_EPS : d > -MandelPrecision.PRIODICITY_EPS) {
                    // Period found.

                    pointInfo.inside = true;
                    pointInfo.lastIter = i; // & check
                    pointInfo.lastZX = zx;
                    pointInfo.lastZY = zy;
                    return;
                }
            }
            if ((i & check) == 0) {
                if (--whenupdate == 0) {
                    whenupdate = 10;
                    check <<= 1;
                    check++;
                }
                // period = 0;
                hx = zx.hi;
                hy = zy.hi;
            }
        }

        // Maxiter reached.
        pointInfo.inside = true;
        pointInfo.lastIter = maxiter;
        pointInfo.lastZX = zx;
        pointInfo.lastZY = zy;
        return;
    }
}