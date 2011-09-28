package digisoft.custom.swing.window;

import java.awt.DisplayMode;

import digisoft.custom.swing.GraphicsFunctions;
import digisoft.custom.swing.window.canvas.MemoryImageSourceCanvas;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/04/04
 */
public class PixelWindow extends MemoryImageSourceCanvas {

    private CanvasWindow window;
    private DisplayMode backupMode = null;

    /**
     * Creates a fullscreen CanvasWindow using a given GraphicsCanvas
     * 
     * 
     * @param x
     *            the x-position of the upper-left corner of the window
     * @param y
     *            the y-position of the upper-left corner of the window
     * @param width
     *            the width of the drawable area of the window
     * @param height
     *            the height of the drawable area of the window
     */
    public PixelWindow(int x, int y, int width, int height) {
        super();

        window = new CanvasWindow(this, x, y, width, height);
    }

    /**
     * Creates a fullscreen CanvasWindow using a given GraphicsCanvas
     * 
     * 
     * @param width
     *            the width of the drawable area of the window
     * @param height
     *            the height of the drawable area of the window
     */
    public PixelWindow(int width, int height) {
        super();

        window = new CanvasWindow(this, width, height);
    }

    /**
     * Creates a fullscreen CanvasWindow using a given GraphicsCanvas
     * 
     * @param exclusive
     *            when this is set, the window becomes fullscreen, otherwise, it
     *            will stretch to fit the screen.
     */
    public PixelWindow(boolean exclusive) {
        super();

        window = new CanvasWindow(this, exclusive);
    }

    public PixelWindow(DisplayMode mode) {
        super();

        backupMode = GraphicsFunctions.getDisplayDevice().getDisplayMode();

        window = new CanvasWindow(this, mode);

        while (pixels == null) {
            Thread.yield();
        }
    }

    public void setExclusive(boolean b) {
        window.setExclusive(b);
    }

    public void setFullscreen(DisplayMode mode) {
        if (backupMode == null) {
            window.setExclusive(true);
            backupMode = GraphicsFunctions.getDisplayDevice().getDisplayMode();
            GraphicsFunctions.getDisplayDevice().setDisplayMode(mode);
        } else {
            GraphicsFunctions.getDisplayDevice().setDisplayMode(backupMode);
            window.setExclusive(false);
            backupMode = null;
        }
    }

    public void setTitle(String name) {
        window.setTitle(name);
    }
}
