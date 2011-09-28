package deepZoom.fractals;

import deepZoom.renderer.PointInfo;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/07/09
 */
public class Mandel extends Fractal {

    private static final double PRIODICITY_EPS = 1e-17;

    @Override
    public void initFrame() {
    }

    @Override
    public void calcPoint(PointInfo pointInfo) {
        double zx;
        double zy;
        double xx;
        double yy;

        double bailout = parameters.getBailout();
        long maxiter = parameters.getMaxiter();

        // Calculate viewport.
        double px = pointInfo.px.hi;
        double py = pointInfo.py.hi;

        // Main bulb check.
        zx = 4 * (px * px + py * py);
        xx = 2 * px;
        zy = zx + 4 * xx;
        if (zy < -3.75) {
            pointInfo.inside = true;
            pointInfo.lastIter = maxiter;
            pointInfo.lastZX.hi = px;
            pointInfo.lastZY.hi = py;
            return;
        }

        // Cardoid check.
        zy = zx - xx + 0.25;
        xx = xx + zy - 0.5;
        xx *= xx;
        if (zy > xx) {
            pointInfo.inside = true;
            pointInfo.lastIter = maxiter;
            pointInfo.lastZX.hi = px;
            pointInfo.lastZY.hi = py;
            return;
        }

        zx = px;
        zy = py;

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
            xx = zx * zx;
            yy = zy * zy;

            // Check bailout.
            if (xx + yy > bailout) {
                pointInfo.inside = false;
                pointInfo.lastIter = i;
                pointInfo.lastZX.hi = zx;
                pointInfo.lastZY.hi = zy;
                return;
            }

            // Iterate
            zy = 2 * zx * zy + py;
            zx = xx - yy + px;

            // Periodicity check.
            double d = zx - hx;
            if (d > 0.0 ? d < Mandel.PRIODICITY_EPS : d > -Mandel.PRIODICITY_EPS) {
                d = zy - hy;
                if (d > 0.0 ? d < Mandel.PRIODICITY_EPS : d > -Mandel.PRIODICITY_EPS) {
                    // Period found.

                    pointInfo.inside = true;
                    pointInfo.lastIter = i; // & check
                    pointInfo.lastZX.hi = zx;
                    pointInfo.lastZY.hi = zy;
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
                hx = zx;
                hy = zy;
            }
        }

        // Maxiter reached.
        pointInfo.inside = true;
        pointInfo.lastIter = maxiter;
        pointInfo.lastZX.hi = zx;
        pointInfo.lastZY.hi = zy;
        return;
    }
}