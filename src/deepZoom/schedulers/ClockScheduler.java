package deepZoom.schedulers;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/05/02
 */
public class ClockScheduler extends Scheduler {

    @Override
    protected void initImpl() {
        super.clear();

        for (int i = 0; i < numLayers; i++) {
            for (int y = 0; y < height; y++) {
                double dy = y - (height - 0.875) * 0.5;
                for (int x = 0; x < width; x++) {
                    double dx = x - (width - 0.75) * 0.5;
                    add(new PriorityPoint(i, x, y, Math.atan2(-dy, -dx)));
                }
            }
        }
    }
}
