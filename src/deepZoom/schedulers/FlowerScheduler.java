package deepZoom.schedulers;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/05/03
 */
public class FlowerScheduler extends Scheduler {

    private int petals;
    private double petalSize;

    public FlowerScheduler(int petals, double petalSize) {
        this.petals = petals;
        this.petalSize = 4 / petalSize + 1;
    }

    @Override
    protected void initImpl() {
        super.clear();

        for (int i = 0; i < numLayers; i++) {
            for (int y = 0; y < height; y++) {
                double dy = y - (height - 0.875) * 0.5;
                for (int x = 0; x < width; x++) {
                    double dx = x - (width - 0.75) * 0.5;

                    double a = StrictMath.atan2(dy, -dx) * petals;
                    double r = StrictMath.sqrt(dx * dx + dy * dy);
                    dx = r * (StrictMath.cos(a) + petalSize);
                    add(new PriorityPoint(i, x, y, dx));
                }
            }
        }
    }
}
