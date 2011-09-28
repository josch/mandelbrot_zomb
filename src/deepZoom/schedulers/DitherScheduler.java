package deepZoom.schedulers;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/05/03
 */
public class DitherScheduler extends Scheduler {

    @Override
    protected void initImpl() {
        super.clear();

        for (int i = 0; i < numLayers; i++) {
            for (int y = 0; y < height; y++) {
                int dy = Integer.reverse(y) >>> 16;
                for (int x = 0; x < width; x++) {
                    int dx = Integer.reverse(x) >>> 16;

                    add(new PriorityPoint(i, x, y, dx + dy));
                }
            }
        }
    }
}
