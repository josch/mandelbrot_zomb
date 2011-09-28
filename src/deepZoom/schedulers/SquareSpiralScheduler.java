package deepZoom.schedulers;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/05/04
 */
public class SquareSpiralScheduler extends Scheduler {

    @Override
    protected void initImpl() {
        super.clear();

        for (int i = 0; i < numLayers; i++) {
            for (int y = 0; y < height; y++) {
                int dy = y - (height >> 1);
                for (int x = 0; x < width; x++) {
                    int dx = x - (width >> 1);

                    if (dx > dy) {
                        if (dx < -dy) {
                            dx = 4 * dy * dy + 7 * dy + 3 + dx;
                        } else {
                            dx = 4 * dx * dx + 3 * dx + 1 + dy;
                        }
                    } else if (dx < -dy) {
                        dx = 4 * dx * dx + 1 * dx - 1 - dy;
                    } else {
                        dx = 4 * dy * dy + 5 * dy + 1 - dx;
                    }

                    add(new PriorityPoint(i, x, y, dx));
                }
            }
        }
    }
}
