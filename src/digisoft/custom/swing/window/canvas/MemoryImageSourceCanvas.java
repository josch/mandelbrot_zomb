package digisoft.custom.swing.window.canvas;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Arrays;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/03/22
 */
public class MemoryImageSourceCanvas extends GraphicsCanvas {

    private Image image;
    public int[] pixels;
    public int pixelCount;
    private ColorModel cm;
    private MemoryImageSource source;

    // ////////////////////////////////////////////////////////////
    @Override
    public void clear(int backgroundColor) {
        Arrays.fill(pixels, backgroundColor);
    }

    @Override
    public void pset(int x, int y, int rgb) {
        pixels[y * width + x] = rgb;
    }

    @Override
    public int pget(int x, int y) {
        return pixels[y * width + x];
    }

    // ////////////////////////////////////////////////////////////
    @Override
    protected void initImpl(int width, int height) {
        pixelCount = width * height;

        if (pixels == null || pixels.length < pixelCount) {
            pixels = new int[pixelCount];
        }

        cm = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff);
        source = new MemoryImageSource(width, height, pixels, 0, width);
        source.setFullBufferUpdates(true);
        source.setAnimated(true);
        image = Toolkit.getDefaultToolkit().createImage(source);
    }

    @Override
    public void paintImpl(Graphics g) {
        if (source != null) {
            source.newPixels(pixels, cm, 0, width);
        }

        if (image != null) {
            g.drawImage(image, 0, 0, null);

            // force repaint now (proper method)
            // Toolkit.getDefaultToolkit().sync();
        }
    }
}
