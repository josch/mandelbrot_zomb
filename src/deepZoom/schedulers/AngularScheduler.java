package deepZoom.schedulers;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/05/02
 */
public class AngularScheduler extends Scheduler {

    private int bands;

    public AngularScheduler(int bands) {
        this.bands = bands;
    }

    @Override
    protected void initImpl() {
        super.clear();

        for (int i = 0; i < numLayers; i++) {
            for (int y = 0; y < height; y++) {
                double dy = y - (height - 0.875) * 0.5;
                for (int x = 0; x < width; x++) {
                    double dx = x - (width - 0.75) * 0.5;

                    double a = StrictMath.abs((StrictMath.atan2(dy, -dx) / (2 * StrictMath.PI) + 0.5) * bands % 1 - 0.5);
                    add(new PriorityPoint(i, x, y, -a));
                }
            }
        }
    }
}
