package deepZoom.schedulers;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/05/02
 */
public class SpiralScheduler extends Scheduler {

    private double thickness;

    public SpiralScheduler(double thickness) {
        this.thickness = thickness;
    }

    @Override
    protected void initImpl() {
        super.clear();

        for (int i = 0; i < numLayers; i++) {
            for (int y = 0; y < height; y++) {
                double dy = y - (height - 0.875) * 0.5;
                for (int x = 0; x < width; x++) {
                    double dx = x - (width - 0.75) * 0.5;

                    double a = StrictMath.atan2(dy, -dx) / (2 * StrictMath.PI);
                    dx = (int) (a + StrictMath.sqrt(dx * dx + dy * dy) / thickness) * thickness - a;
                    add(new PriorityPoint(i, x, y, dx));
                }
            }
        }
    }
}
