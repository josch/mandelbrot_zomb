package deepZoom.schedulers;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/05/03
 */
public class SplitScheduler extends Scheduler {

    @Override
    protected void initImpl() {
        super.clear();

        for (int i = 0; i < numLayers; i++) {
            for (int y = 0; y < height; y++) {
                int dy = StrictMath.abs(y - (height >> 1)) * width;
                for (int x = 0; x < width; x++) {
                    add(new PriorityPoint(i, x, y, dy + x));
                }
            }
        }
    }
}
