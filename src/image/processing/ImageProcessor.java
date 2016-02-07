package image.processing;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes images
 *
 * @author Rakib
 */
public class ImageProcessor {
    private Image image;

    /**
     * Constructor
     *
     * @param img The image to process
     */
    public ImageProcessor(Image img) {
        BufferedImage originalImage = getBufferedImage(img);
        BufferedImage copyOfImage =
                new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(originalImage, 0, 0, null);

        this.image = (Image) copyOfImage;
    }

    /**
     * Generates image src data to display in html
     *
     * @return String as base64 encoded string
     * @throws IOException
     */
    public String getSrcData() throws IOException {
        byte[] imageBytes = getImgBytes(this.image);

        BASE64Encoder encoder = new BASE64Encoder();
        String base64 = encoder.encode(imageBytes);
        return "data:image/jpeg;base64," + base64;
    }

    /**
     * Selects points by matching color
     *
     * @param color     The selected color
     * @param tolerance Tolerance value for matching
     * @return List of matched colors
     */
    public List<Point> getPixelsByColor(Color color, float tolerance) {
        int tolInt = (int) (tolerance * 1000000);
        int colorRGB = color.getRGB() | 0xFF000000;
        BufferedImage img = getBufferedImage(this.image);

        List<Point> lst = new ArrayList<Point>();

        int w = img.getWidth();
        int h = img.getHeight();

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pixel = img.getRGB(j, i);

                if (Math.abs(pixel - colorRGB) < tolInt) {
                    lst.add(new Point(j, i));
                }
            }
        }

        return lst;
    }

    /**
     * Draws line among points
     *
     * @param points List of points to draw line within
     * @param color  Color of the line
     */
    public void drawLine(List<Point> points, Color color) {
        Graphics2D g2d = (Graphics2D) this.image.getGraphics();
        g2d.setColor(color);

        for (int i = 0; i < points.size(); i++) {
            int prevIndex = (i - 1);
            Point prevPoint = points.get((prevIndex < 0) ? (points.size() - 1) : prevIndex);
            Point curPoint = points.get(i);
            g2d.drawLine((int) prevPoint.getX(), (int) prevPoint.getY(), (int) curPoint.getX(), (int) curPoint.getY());
        }
        g2d.finalize();
    }

    /**
     * Gets points by detecting edges
     *
     * @param level Level of sharpness of edges
     * @return List of edge points
     */
    public List<Point> getEdges(float level) {
        return PointProcessor.getEdge(getBufferedImage(this.image), level);
    }

    /**
     * Gets byte array of an image
     *
     * @param image The image object
     * @return Byte array
     */
    private byte[] getImgBytes(Image image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(getBufferedImage(image), "jpeg", baos);
        } catch (IOException ex) {
            //handle it here.... not implemented yet...
        }
        return baos.toByteArray();
    }

    /**
     * Converts an Image object into a BufferedImage
     *
     * @param image The Image object
     * @return BufferedImage
     */
    private BufferedImage getBufferedImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        return bi;
    }
}
