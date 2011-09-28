package digisoft.custom.awt;

import java.awt.Color;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2005/10/03
 */
public class Color3f {

    private static final float MIN_CLIP_LEVEL = -0.003921568627450980392156862745098f;
    private static final float MAX_CLIP_LEVEL = 1.003921568627450980392156862745098f;
    public float r;
    public float g;
    public float b;

    /**
     * Create the color opaque black
     */
    public Color3f() {
        this(0f, 0f, 0f);
    }

    /**
     * Create an opaque color from packed RGB triple
     * 
     * @param i
     * @since 1.0
     */
    public Color3f(int i) {
        b = (i & 0xFF) / 255f;
        g = ((i >>= 8) & 0xFF) / 255f;
        r = (i >> 8 & 0xFF) / 255f;
    }

    /**
     * Create an opaque color defined gray shade
     * 
     * @param i
     * @since 1.0
     */
    public Color3f(float i) {
        this(i, i, i);
    }

    /**
     * Create an opaque defined color
     * 
     * @param r
     * @param g
     * @param b
     */
    public Color3f(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Create an opaque defined color
     * 
     * @param r
     * @param g
     * @param b
     */
    public Color3f(int r, int g, int b) {
        this.r = r / 255f;
        this.g = g / 255f;
        this.b = b / 255f;
    }

    /**
     * @param color
     * @since 1.0
     */
    public Color3f(Color color) {
        r = color.getRed() / 255f;
        g = color.getGreen() / 255f;
        b = color.getBlue() / 255f;
    }

    /**
     * @param color
     * @since 1.0
     */
    public Color3f(Color3f color) {
        r = color.r;
        g = color.g;
        b = color.b;
    }

    public void set(Color3f c) {
        r = c.r;
        g = c.g;
        b = c.b;
    }

    public void set(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public float getGrayValue() {
        return 0.299f * r + 0.587f * g + 0.114f * b;
    }

    public void addSelf(int i) {
        b += (i & 0xFF) / 255f;
        g += ((i >>= 8) & 0xFF) / 255f;
        r += (i >> 8 & 0xFF) / 255f;
    }

    public Color3f scale(float f) {
        return new Color3f(r * f, g * f, b * f);
    }

    public void scaleSelf(float f) {
        r *= f;
        g *= f;
        b *= f;
    }

    public Color3f interpolateLinear(Color3f c, float f) {
        return new Color3f( //
                r + (c.r - r) * f, //
                g + (c.g - g) * f, //
                b + (c.b - b) * f);
    }

    public void interpolateLinearSelf(Color3f c, float f) {
        r = r + (c.r - r) * f;
        g = g + (c.g - g) * f;
        b = b + (c.b - b) * f;
    }

    public Color3f gamma(float gammaFactor) {
        return new Color3f( //
                (float) Math.pow(r, gammaFactor), //
                (float) Math.pow(g, gammaFactor), //
                (float) Math.pow(b, gammaFactor));
    }

    /**
     * Converts the components of this color, as specified by the HSB model, to
     * an equivalent set of values for the default RGB model.
     * <p>
     * The <code>saturation</code> and <code>brightness</code> components should
     * be floating-point values between zero and one (numbers in the range
     * 0.0-1.0). The <code>hue</code> component can be any floating-point
     * number. The floor of this number is subtracted from it to create a
     * fraction between 0 and 1. This fractional number is then virtually
     * multiplied by 360 to produce the hue angle in the HSB color model.
     * <p>
     * 
     * @param hsb
     *            the Color3f containing the components hue, saturation,
     *            brightness and alpha in the fields of red, green, blue and
     *            alpha, respectively
     * @return the Color3f with the RGB value of the color with the indicated
     *         hue, saturation, brightness and alpha.
     * 
     * @see java.awt.Color#HSB2RGB(float, float, float)
     * @see java.awt.Color#getRGB()
     * @see java.awt.Color#Color(int)
     * @see java.awt.image.ColorModel#getRGBdefault()
     * @since 1.0
     */
    public Color3f hsb2Rgb() {
        if (g == 0) {
            return new Color3f(b, b, b);
        }
        Color3f out = new Color3f(0, 0, 0);
        float hue6 = (r - (float) Math.floor(r)) * 6;
        float hue_f = hue6 - (float) java.lang.Math.floor(hue6);
        float p = b * (1 - g);
        switch ((int) hue6) {
            case 0:
            case 6: {
                out.r = b;
                out.g = b * (1 - g * (1 - hue_f));
                out.b = p;
                break;
            }
            case 1: {
                out.r = b * (1 - g * hue_f);
                out.g = b;
                out.b = p;
                break;
            }
            case 2: {
                out.r = p;
                out.g = b;
                out.b = b * (1 - g * (1 - hue_f));
                break;
            }
            case 3: {
                out.r = p;
                out.g = b * (1 - g * hue_f);
                out.b = b;
            }
            break;
            case 4: {
                out.r = b * (1 - g * (1 - hue_f));
                out.g = p;
                out.b = b;
                break;
            }
            case 5: {
                out.r = b;
                out.g = p;
                out.b = b * (1 - g * hue_f);
            }
        }
        return out;
    }

    /**
     * Converts the components of this color, as specified by the default RGB
     * model, to an equivalent set of values for hue, saturation, and brightness
     * that are the three components of the HSB model.
     * 
     * @see java.awt.Color#RGB2HSB(int, int, int, float[])
     * @see java.awt.Color#getRGB()
     * @see java.awt.Color#Color(int)
     * @see java.awt.image.ColorModel#getRGBdefault()
     * @since JDK1.0
     */
    public Color3f rgb2Hsb() {
        float cmax = r > g ? r : g;
        if (b > cmax) {
            cmax = b;
        }
        float cmin = r < g ? r : g;
        if (b < cmin) {
            cmin = b;
        }
        float dist = cmax - cmin;

        Color3f out = new Color3f(0f, 0f, 0f);

        out.b = cmax;
        out.g = cmax != 0 ? dist / cmax : 0;

        if (out.g == 0) {
            out.r = 0;
        } else {
            if (r == cmax) {
                if (g >= b) {
                    out.r = (g - b) / dist;
                } else {
                    out.r = 6.0f + (g - b) / dist;
                }
            } else if (g == cmax) {
                out.r = 2.0f + (b - r) / dist;
            } else {
                out.r = 4.0f + (r - g) / dist;
            }
            out.r /= 6.0f;
        }
        return out;
    }

    /**
     * Converts the components of this color, as specified by the default RGB
     * model, to an equivalent set of values for hue, saturation, and brightness
     * that are the three components of the HSB model.
     * 
     * @see java.awt.Color#RGB2HSB(int, int, int, float[])
     * @see java.awt.Color#getRGB()
     * @see java.awt.Color#Color(int)
     * @see java.awt.image.ColorModel#getRGBdefault()
     * @since JDK1.0
     */
    public void rgb2HsbSelf() {
        float cmax = r > g ? r : g;
        if (b > cmax) {
            cmax = b;
        }
        float cmin = r < g ? r : g;
        if (b < cmin) {
            cmin = b;
        }
        float dist = cmax - cmin;

        float g = cmax != 0 ? dist / cmax : 0;

        if (g == 0) {
            r = 0;
        } else {
            if (r == cmax) {
                if (this.g >= b) {
                    r = (this.g - b) / dist;
                } else {
                    r = 6.0f + (this.g - b) / dist;
                }
            } else if (this.g == cmax) {
                r = 2.0f + (b - r) / dist;
            } else {
                r = 4.0f + (r - this.g) / dist;
            }
            r /= 6.0f;
        }
        b = cmax;
        this.g = g;
    }

    /**
     * Convert RGB to YPbPr (the floating point version of YCbCr).
     * 
     * @return R=Y in [0, 1]; G=Pb in [-0.5 0.5]; B=Pr in [-0.5 0.5]
     * @since 1.0
     */
    public Color3f rgb2YPbPr() {
        return new Color3f( //
                +0.29900000000000000000f * r + 0.58700000000000000000f * g + 0.114000000000000000000f * b, //
                -0.16873589164785553047f * r - 0.33126410835214446952f * g + 0.500000000000000000000f * b, //
                +0.50000000000000000000f * r - 0.41868758915834522111f * g - 0.081312410841654778888f * b);
    }

    /**
     * Convert RGB to YPbPr (the floating point version of YCbCr).
     * 
     * @return R=Y in [0, 1]; G=Pb in [-0.5 0.5]; B=Pr in [-0.5 0.5]
     * @since 1.0
     */
    public void rgb2YPbPrSelf() {
        float r = +0.29900000000000000000f * this.r + 0.58700000000000000000f * g + 0.114000000000000000000f * b;
        float g = -0.16873589164785553047f * this.r - 0.33126410835214446952f * this.g + 0.500000000000000000000f * b;
        b = +0.50000000000000000000f * this.r - 0.41868758915834522111f * this.g - 0.081312410841654778888f * b;
        this.r = r;
        this.g = g;
    }

    /**
     * Convert YPbPr (the floating point version of YCbCr) to RGB. Inputs: R=Y
     * in [0 1]; G=Pb in [-0.5 0.5]; B=Pr in [-0.5 0.5]
     * 
     * @return RGB colors
     * @since 1.0
     */
    public Color3f tPbPr2Rgb() {
        return new Color3f( //
                r /* ***************************** */ + 1.40200000000000000000f * b, //
                r - 0.34413628620102214650f * g - 0.71413628620102214645f * b, //
                r + 1.77200000000000000000f * g /*
                 * **********************
                 * ******
                 */);
    }

    /**
     * Convert RGB to YUV (the colorspace used by PAL TV).
     * 
     * @return R=Y in [0, 1]; G=U in [-0.5 0.5]; B=V in [-0.5 0.5]
     * @since 1.0
     */
    public Color3f rgb2Yuv() {
        return new Color3f( //
                +0.29900000000000000000f * r + 0.58700000000000000000f * g + 0.11400000000000000000f * b, //
                -0.14713769751693002257f * r - 0.28886230248306997742f * g + 0.43600000000000000000f * b, //
                +0.61500000000000000000f * r - 0.51498573466476462197f * g - 0.10001426533523537803f * b);
    }

    /**
     * Convert YUV (the colorspace used in PAL TV) to RGB. Inputs: R=Y in [0 1];
     * G=U in [-0.5 0.5]; B=V in [-0.5 0.5]
     * 
     * @return RGB colors
     * @since 1.0
     */
    public Color3f yuv2Rgb() {
        return new Color3f( //
                r /* ***************************** */ + 1.13983739837398373980f * b, //
                r - 0.39465170435897035149f * g - 0.58059860666749768007f * b, //
                r + 2.03211009174311926600f * g /*
                 * **********************
                 * ******
                 */);
    }

    /**
     * Convert RGB to YUV (the colorspace used by PAL TV).
     * 
     * @return R=Y in [0, 1]; G=U in [-0.5 0.5]; B=V in [-0.5 0.5]
     * @since 1.0
     */
    public Color3f rgb2YiQ() {
        return new Color3f( //
                +0.29900000000000000000f * r + 0.58700000000000000000f * g + 0.11400000000000000000f * b, //
                +0.59571613491277455268f * r - 0.27445283783925646357f * g - 0.32126329707351808911f * b, //
                +0.21145640212011786639f * r - 0.52259104529161116836f * g + 0.31113464317149330198f * b);
    }

    /**
     * Convert YIQ (the colorspace used in NTSC TV) to RGB. Inputs: R=Y in [0
     * 1]; G=I in [-0.5 0.5]; B=Q in [-0.5 0.5]
     * 
     * @return RGB colors
     * @since 1.0
     */
    public Color3f yiq2Rgb() {
        return new Color3f( //
                r + 0.95629483232089399045f * g + 0.62102512544472871408f * b, //
                r - 0.27212147408397731948f * g - 0.64738095351761572226f * b, //
                r - 1.10698990856712821590f * g + 1.70461497549882932850f * b);
    }

    /**
     * Convert RGB to XYZ (the CIE 1931 color space).
     * 
     * @return R=Y in [0, 1]; G=U in [0 1]; B=V in [0 1]
     * @since 1.0
     */
    public Color3f rgb2Xyz() {
        return new Color3f( //
                +0.43030294260923249074f * r + 0.34163640134469669562f * g + 0.17822777850125160973f * b, //
                +0.22187495478288550304f * r + 0.70683393381661385301f * g + 0.071291111400500643890f * b, //
                +0.020170450434807773004f * r + 0.12958622119971253972f * g + 0.93866630010659181124f * b);
    }

    /**
     * Convert XYZ (the CIE 1931 color space) to RGB. Inputs: R=X in [0 1]; G=Y
     * in [0 1]; B=z in [0 1]
     * 
     * @return RGB colors
     * @since 1.0
     */
    public Color3f xyz2Rgb() {
        return new Color3f( //
                +2.0608465608465608465f * r - 0.93738977072310405639f * g - 0.32010582010582010580f * b, //
                -1.1415343915343915346f * r + 2.2094356261022927693f * g + 0.048941798941798941806f * b, //
                +0.080687830687830687828f * r - 0.27204585537918871252f * g + 1.2711640211640211641f * b);
    }

    public Color3f clip() {
        float ra = r;
        float ga = g;
        float ba = b;

        if (ra < 0) {
            ra = 0;
        } else if (ra > 1) {
            ra = 1;
        }
        if (ga < 0) {
            ga = 0;
        } else if (ga > 1) {
            ga = 1;
        }
        if (ba < 0) {
            ba = 0;
        } else if (ba > 1) {
            ba = 1;
        }

        return new Color3f(ra, ga, ba);
    }

    public Color3f showClip() {
        if (r < Color3f.MIN_CLIP_LEVEL || r > Color3f.MAX_CLIP_LEVEL || g < Color3f.MIN_CLIP_LEVEL || g > Color3f.MAX_CLIP_LEVEL || b < Color3f.MIN_CLIP_LEVEL
                || b > Color3f.MAX_CLIP_LEVEL) {
            return new Color3f(0.5f, 0.5f, 0.5f);
        }
        return this;
    }

    public boolean isClipping() {
        return r < Color3f.MIN_CLIP_LEVEL || r > Color3f.MAX_CLIP_LEVEL || g < Color3f.MIN_CLIP_LEVEL || g > Color3f.MAX_CLIP_LEVEL
                || b < Color3f.MIN_CLIP_LEVEL || b > Color3f.MAX_CLIP_LEVEL;
    }

    /**
     * Converts this color to a color understood by the java.awt package.
     * 
     * @return an instance of java.awt.Color with the color attributes clamped
     *         to {black,white}
     * @see java.awt.Color
     * @since 1.0
     */
    public Color toColor() {
        float ra = r;
        float ga = g;
        float ba = b;

        if (ra < 0) {
            ra = 0;
        } else if (ra > 1) {
            ra = 1;
        }
        if (ga < 0) {
            ga = 0;
        } else if (ga > 1) {
            ga = 1;
        }
        if (ba < 0) {
            ba = 0;
        } else if (ba > 1) {
            ba = 1;
        }

        return new java.awt.Color(ra, ga, ba);
    }

    public int getRGB() {
        int ra = (int) (r * 255 + 0.5);
        int ga = (int) (g * 255 + 0.5);
        int ba = (int) (b * 255 + 0.5);

        if (ra < 0) {
            ra = 0;
        } else if (ra > 255) {
            ra = 255;
        }
        if (ga < 0) {
            ga = 0;
        } else if (ga > 255) {
            ga = 255;
        }
        if (ba < 0) {
            ba = 0;
        } else if (ba > 255) {
            ba = 255;
        }

        return ba + (ga + (ra << 8) << 8);
    }

    @Override
    public String toString() {
        return super.getClass().getSimpleName() + "[" + r + ", " + g + ", " + b + "]";
    }
}
