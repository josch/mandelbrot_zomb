package digisoft.custom.swing.window.canvas;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/03/22
 */
public abstract class GraphicsCanvas extends JPanel implements ComponentListener {

    public int width;
    public int height;
    public int halfWidth;
    public int halfHeight;
    private volatile boolean resizeRequest;
    protected Graphics pg = null;

    // ////////////////////////////////////////////////////////////
    public GraphicsCanvas() {
        addComponentListener(this);
    }

    public void init(int width, int height, boolean painting) {
        if (this.width != width || this.height != height) {
            setPreferredSize(new Dimension(width, height));

            initImpl(width, height);

            this.width = width;
            this.height = height;

            halfWidth = width >> 1;
            halfHeight = height >> 1;

            g = null;

            if (painting) {
                resized();
            }
        }
    }

    // ////////////////////////////////////////////////////////////
    public void clear(Paint paint) {
        throw new Error("Engine " + this.getClass().getSimpleName() + " has no clear(Paint paint)");
    }

    public void clear(int rgb) {
        throw new Error("Engine " + this.getClass().getSimpleName() + " has no clear(int rgb)");
    }

    public void clear(int rgba, boolean hasAlpha) {
        throw new Error("Engine " + this.getClass().getSimpleName() + " has no clear(int rgba, boolean hasAlpha)");
    }

    public void pset(int x, int y, int rgb) {
        throw new Error("Engine " + this.getClass().getSimpleName() + " has no pset(int x, int y, int rgb)");
    }

    public int pget(int x, int y) {
        throw new Error("Engine " + this.getClass().getSimpleName() + " has no pget(int x, int y)");
    }

    public Graphics2D graphics() {
        throw new Error("Engine " + this.getClass().getSimpleName() + " has no 2D graphics. Only pixel access supported.");
    }

    public void setAntialiased(boolean b) {
        throw new Error("Engine " + this.getClass().getSimpleName() + " has no 2D graphics. No antialiasing supported.");
    }

    protected abstract void initImpl(int width, int height);

    protected abstract void paintImpl(Graphics g);

    public boolean ready() {
        return true;
    }

    // ////////////////////////////////////////////////////////////
    public void resized() {
    }

    // ////////////////////////////////////////////////////////////
    @Override
    public void paint(Graphics g) {
        if (resizeRequest) {
            init(super.getWidth(), super.getHeight(), true);
            resizeRequest = false;
        } else {
            paintImpl(g);
        }
    }
    private Graphics2D g = null;

    public void repaintNow() {
        if (g == null) {
            g = (Graphics2D) getGraphics();
        }
        paint(g);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        System.out.println("componentResized " + width + "\t" + height);
        if (width != super.getWidth() || height != super.getHeight()) {
            resizeRequest = true;
        }
        super.repaint();
    }

    @Override
    public void componentHidden(ComponentEvent arg0) {
    }

    @Override
    public void componentMoved(ComponentEvent arg0) {
    }

    @Override
    public void componentShown(ComponentEvent arg0) {
    }
}
