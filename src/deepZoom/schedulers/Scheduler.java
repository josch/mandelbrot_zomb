package deepZoom.schedulers;

import java.util.concurrent.PriorityBlockingQueue;

import deepZoom.viewports.Viewport;
import java.util.PriorityQueue;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/04/17
 */
public abstract class Scheduler extends PriorityBlockingQueue<PriorityPoint> {

    protected int numLayers = 1;
    protected int width;
    protected int height;
    private int total;
    private Viewport viewport;

    public void setNumLayers(int numLayers) {
        this.numLayers = numLayers;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public void init() {
        width = viewport.width;
        height = viewport.height;

        initImpl();

        total = super.size();
    }

    protected abstract void initImpl();

    public double getProgress() {
        return (total - super.size()) / (double) total;
    }
}
