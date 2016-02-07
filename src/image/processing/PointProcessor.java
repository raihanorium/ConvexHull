package image.processing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Processes Points
 *
 * @author Rakib
 */
public abstract class PointProcessor {
    public static final int TOLERANCE = 1;

    /**
     * Sorts points by x coordinate
     *
     * @param list List to sort
     */
    private static void sortByXCoordinates(List<Point> list) {
        Collections.sort(list, new PointCompare());
    }

    /**
     * Detects edge in an image
     *
     * @param image The image to work with
     * @param level Level of sharpness
     * @return List as the edge points
     */
    public static java.util.List<Point> getEdge(BufferedImage image, float level) {
        List<Point> result = new ArrayList<Point>();

        for (int y = 0; y < (image.getHeight() - 1); y++) {
            for (int x = 0; x < (image.getWidth() - 1); x++) {
                int colorCurPx = image.getRGB(x, y);
                int colorRightPx = image.getRGB(x + 1, y);
                int colorBelowPx = image.getRGB(x, y + 1);

                float pxLum = getLuminance(colorCurPx);
                float rightLum = getLuminance(colorRightPx);
                float belowLum = getLuminance(colorBelowPx);

                if ((rightLum - pxLum) > level && (belowLum - pxLum) > level) {
                    result.add(new Point(x, y));
                }
            }
        }

        return result;
    }

    /**
     * Determines the luminance of a color
     *
     * @param color Integer value of a color
     * @return Luminance in float
     */
    private static float getLuminance(int color) {
        // extract each color component
        int red = (color >>> 16) & 0xFF;
        int green = (color >>> 8) & 0xFF;
        int blue = (color >>> 0) & 0xFF;

        // calc luminance in range 0.0 to 1.0; using SRGB luminance constants
        float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;
        return luminance;
    }
}

/**
 * Compares points for sorting
 */
class PointCompare implements Comparator<Point> {

    /**
     * Compares two points for sorting
     *
     * @param a First point
     * @param b Second point
     * @return Comparison result as int
     */
    public int compare(final Point a, final Point b) {
        if (a.x < b.x) {
            return -1;
        } else if (a.x > b.x) {
            return 1;
        } else {
            return 0;
        }
    }
}