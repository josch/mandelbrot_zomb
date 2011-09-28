package digisoft.custom.swing.gradient;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/04/19
 */
public interface Gradient {

    public boolean hasTransparency();

    public int getLength();

    public int get(int index);

    public int get(double position);

    public int getOverflow();
}
