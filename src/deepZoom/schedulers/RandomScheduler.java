package deepZoom.schedulers;

import digisoft.custom.NumberFunctions;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/05/02
 */
public class RandomScheduler extends Scheduler {

    private int area = -1;
    private double[] order;
    private int position = 0;

    @Override
    protected void initImpl() {
        if (area != width * height) {
            area = width * height;

            order = new double[area];
            for (int i = 0; i < area; i++) {
                order[i] = NumberFunctions.RND.nextDouble();
            }
        }

        super.clear();

        int p = position;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int i = 0; i < numLayers; i++) {
                    add(new PriorityPoint(i, x, y, order[p]));
                }
                p = (p + 1) % area;
            }
        }
    }

    @Override
    public PriorityPoint poll() {
        position = (position + area - 1) % area;
        return super.poll();
    }
}
