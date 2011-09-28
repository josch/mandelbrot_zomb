package digisoft.custom.swing.gradient;

import digisoft.custom.awt.Color3f;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/04/05
 */
public class OpaqueGradient implements Gradient {

    private static final int MAX_GRADIENT_ARRAY_SIZE = 5000;
    private static final int GRADIENT_SIZE = 256;
    private float[] fractions;
    private int gradientOverflow;
    private float[] intervals;
    private int gradientLength;
    private int[] gradient;

    public OpaqueGradient(float[] fractions, Color3f[] colors) {
        this.fractions = fractions;

        gradientOverflow = colors[colors.length - 1].getRGB();

        intervals = new float[fractions.length - 1];
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = fractions[i + 1] - fractions[i];
        }

        calculateGradientFractions(colors);
    }

    private void calculateGradientFractions(Color3f[] colors) {
        int n = intervals.length;

        // Find smallest interval
        float Imin = 1;
        for (int i = 0; i < n; i++) {
            if (Imin > intervals[i]) {
                Imin = intervals[i];
            }
        }

        calculateSingleArrayGradient(colors, Imin);

    }

    private void calculateSingleArrayGradient(Color3f[] colors, float Imin) {

        gradientLength = (int) (OpaqueGradient.GRADIENT_SIZE / Imin + 0.5);
        if (gradientLength > OpaqueGradient.MAX_GRADIENT_ARRAY_SIZE) {
            gradientLength = OpaqueGradient.MAX_GRADIENT_ARRAY_SIZE;
        }

        gradient = new int[gradientLength + 1];
        int part = 0;
        for (int i = 0; i <= gradientLength; i++) {
            // Fraction of the gradient.
            float f = (float) i / gradientLength;

            // Select interval.
            while (f > fractions[part + 1]) {
                part++;
            }

            // Fraction of the current interval.
            float p = (f - fractions[part]) / intervals[part];

            // 2 colors to interpolate
            Color3f c1 = colors[part];
            Color3f c2 = colors[part + 1];

            gradient[i] = interpolate(c1, c2, p);
        }
    }

    private int interpolate(Color3f c1, Color3f c2, float p) {
        return 0xFF000000 //
                | (int) ((c1.r + (c2.r - c1.r) * p) * 255 + .5) << 16 //
                | (int) ((c1.g + (c2.g - c1.g) * p) * 255 + .5) << 8//
                | (int) ((c1.b + (c2.b - c1.b) * p) * 255 + .5);
    }

    @Override
    public boolean hasTransparency() {
        return false;
    }

    @Override
    public int getLength() {
        return gradientLength;
    }

    @Override
    public int get(int index) {
        return gradient[index];
    }

    @Override
    public int get(double position) {
        return gradient[(int) (position * gradientLength + 0.5)];
    }

    @Override
    public int getOverflow() {
        return gradientOverflow;
    }
}
