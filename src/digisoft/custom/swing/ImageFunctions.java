package digisoft.custom.swing;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author Zom-B
 * @since 1.0
 * @date 2009/04/28
 */
public class ImageFunctions {

    public static final int SCALE_HINT_ALWAYS = 0;
    public static final int SCALE_HINT_FIT_INSIDE = 1;
    public static final int SCALE_HINT_WHEN_NECESSARY = 2;

    public static ImageIcon scaleImage(ImageIcon imageIcon, int width, int height, int scalingHint) {
        switch (scalingHint) {
            case SCALE_HINT_WHEN_NECESSARY: {
                if (imageIcon.getIconWidth() <= width && imageIcon.getIconHeight() <= height) {
                    return imageIcon;
                }
                // Fall through.
            }
            case SCALE_HINT_ALWAYS: {
                return ImageFunctions.scaleImage(imageIcon, width, height);
            }
            case SCALE_HINT_FIT_INSIDE: {
                Image image = imageIcon.getImage();

                double wf = (double) width / image.getWidth(null);
                double hf = (double) height / image.getHeight(null);

                if (wf > hf) {
                    height = (int) (wf * image.getHeight(null) + 0.5);
                } else if (hf > wf) {
                    width = (int) (hf * image.getWidth(null) + 0.5);
                }

                // imageIcon.setImage(image.getScaledInstance(width, height,
                // Image.SCALE_AREA_AVERAGING));
                // return imageIcon;
                return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING));
            }
        }
        throw new IllegalArgumentException("Illegal scalingHint. Must be one of SCALE_HINT_ALWAYS, SCALE_HINT_FIT_INSIDE, SCALE_HINT_WHEN_NECESSARY");
    }

    public static ImageIcon scaleImage(ImageIcon imageIcon, int width, int height) {
        Image image = imageIcon.getImage();

        double wf = (double) width / image.getWidth(null);
        double hf = (double) height / image.getHeight(null);

        if (wf < hf) {
            height = (int) (wf * image.getHeight(null) + 0.5);
        } else if (hf < wf) {
            width = (int) (hf * image.getWidth(null) + 0.5);
        }

        // imageIcon.setImage(image.getScaledInstance(width, height,
        // Image.SCALE_AREA_AVERAGING));
        // return imageIcon;
        return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING));
    }

    public static void saveJPEG(String filename, int[] pixels, int width, int height) throws IOException {
        BufferedImage saveImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        saveImage.setRGB(0, 0, width, height, pixels, 0, width);

        OutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(saveImage);
        param.setQuality(0.95f, false);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(saveImage);
        out.close();
    }

    public static void savePNG(String filename, int[] pixels, int width, int height) throws IOException {
        BufferedImage saveImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        saveImage.setRGB(0, 0, width, height, pixels, 0, width);

        OutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
        ImageIO.write(saveImage, "png", out);

        out.close();
    }
}