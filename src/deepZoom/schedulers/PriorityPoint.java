package deepZoom.schedulers;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/04/17
 */
public class PriorityPoint implements Comparable<PriorityPoint> {

    public int layer;
    public int x;
    public int y;
    private double priority;

    public PriorityPoint(int layer, int x, int y, double priority) {
        this.layer = layer;
        this.x = x;
        this.y = y;
        this.priority = priority;
    }

    @Override
    public int compareTo(PriorityPoint other) {
        double d = priority - other.priority;
        return d < 0 ? -1 : d > 0 ? 1 : 0;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[layer " + layer + ", (" + x + ", " + y + ")]";
    }
}
