package digisoft.custom.swing.window;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import digisoft.custom.swing.GraphicsFunctions;
import digisoft.custom.swing.window.canvas.GraphicsCanvas;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/03/22
 */
public class CanvasWindow extends JFrame {

    /**
     * Creates a fullscreen CanvasWindow using a given GraphicsCanvas
     * 
     * 
     * @param canvas
     *            the GraphicsCanvas defining the drawing method
     * @param x
     *            the x-position of the upper-left corner of the window
     * @param y
     *            the y-position of the upper-left corner of the window
     * @param width
     *            the width of the drawable area of the window
     * @param height
     *            the height of the drawable area of the window
     */
    public CanvasWindow(GraphicsCanvas canvas, int x, int y, int width, int height) {
        super();

        this.setup(canvas, width, height);

        super.setResizable(false);
        super.pack();
        super.setLocation(x, y);
        super.setVisible(true);
        
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(CanvasWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        canvas.requestFocus();
    }

    /**
     * Creates a fullscreen CanvasWindow using a given GraphicsCanvas
     * 
     * 
     * @param canvas
     *            the GraphicsCanvas defining the drawing method
     * @param width
     *            the width of the drawable area of the window
     * @param height
     *            the height of the drawable area of the window
     */
    public CanvasWindow(GraphicsCanvas canvas, int width, int height) {
        super();

        this.setup(canvas, width, height);

        super.setResizable(false);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);

        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(CanvasWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        canvas.requestFocus();
    }

    /**
     * Creates a fullscreen CanvasWindow using a given GraphicsCanvas
     * 
     * @param canvas
     *            the GraphicsCanvas defining the drawing method
     * @param exclusive
     *            when this is set, the window becomes fullscreen, otherwise, it
     *            will stretch to fit the screen.
     */
    public CanvasWindow(GraphicsCanvas canvas, boolean exclusive) {
        super();

        GraphicsDevice displayDevice = GraphicsFunctions.getDisplayDevice();
        DisplayMode mode = displayDevice.getDisplayMode();
        int width = mode.getWidth();
        int height = mode.getHeight();

        this.setup(canvas, width, height);

        super.invalidate();
        super.setUndecorated(true);

        super.setResizable(false);
        super.pack();
        super.setLocationRelativeTo(null);

        if (exclusive) {
            displayDevice.setFullScreenWindow(this);
        } else {
            super.setBounds(new Rectangle(0, 0, width, height));
            super.setVisible(true);
        }

        canvas.requestFocus();
    }

    public CanvasWindow(GraphicsCanvas canvas, DisplayMode mode) {
        super();

        GraphicsDevice displayDevice = GraphicsFunctions.getDisplayDevice();
        int width = mode.getWidth();
        int height = mode.getHeight();

        this.setup(canvas, width, height);

        super.invalidate();
        super.setUndecorated(true);

        super.setResizable(false);
        super.pack();
        super.setLocationRelativeTo(null);

        displayDevice.setFullScreenWindow(this);
        displayDevice.setDisplayMode(mode);

        canvas.requestFocus();
    }

    private void setup(GraphicsCanvas canvas, int width, int height) {
        super.setName(this.getClass().getSimpleName());
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLayout(null);

        {
            canvas.init(width, height, false);

            super.setContentPane(canvas);
        }
    }

    public void setExclusive(boolean b) {
        GraphicsFunctions.getDisplayDevice().setFullScreenWindow(b ? this : null);

        GraphicsCanvas canvas = (GraphicsCanvas) this.getContentPane();
        if (b) {
            super.setBounds(new Rectangle(0, 0, canvas.width, canvas.height));
        } else {
            super.pack();
        }
    }
}
