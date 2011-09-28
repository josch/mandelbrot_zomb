package digisoft.custom.swing;

import java.awt.BasicStroke;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;

import javax.swing.UIManager;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2007/12/01
 */
public class GraphicsFunctions {

    public static final BasicStroke DEFAULT_SQUARE_STROKE = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    public static final BasicStroke DEFAULT_ROUND_STROKE = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public static void setNiceLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }
    }

    public static void setAntialiased(Graphics2D g, boolean antialiased) {
        if (antialiased && g.getRenderingHint(RenderingHints.KEY_ANTIALIASING) != RenderingHints.VALUE_ANTIALIAS_ON) {
            g.translate(0.5, 0.5);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        }
        if (!antialiased && g.getRenderingHint(RenderingHints.KEY_ANTIALIASING) == RenderingHints.VALUE_ANTIALIAS_ON) {
            g.translate(-0.5, -0.5);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
            g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        }
    }

    public static GraphicsDevice getDisplayDevice() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    public static DisplayMode getDisplayMode(int width, int height) {
        DisplayMode currentMode = GraphicsFunctions.getDisplayDevice().getDisplayMode();
        DisplayMode[] modes = GraphicsFunctions.getDisplayDevice().getDisplayModes();

        int bitDepth = currentMode.getBitDepth();
        int refreshRate = currentMode.getRefreshRate();

        for (DisplayMode mode : modes) {
            if (mode.getHeight() == height && mode.getWidth() == width && mode.getBitDepth() == bitDepth && mode.getRefreshRate() == refreshRate) {
                return mode;
            }
        }

        refreshRate = Integer.MAX_VALUE;
        int index = -1;
        for (int i = modes.length - 1; i >= 0; i--) {
            DisplayMode mode = modes[i];
            if (mode.getHeight() == height && mode.getWidth() == width && mode.getBitDepth() == bitDepth) {
                if (refreshRate > mode.getRefreshRate()) {
                    refreshRate = mode.getRefreshRate();
                    index = i;
                }
            }
        }

        return index < 0 ? null : modes[index];
    }
}
