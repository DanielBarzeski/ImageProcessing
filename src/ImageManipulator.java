import java.awt.*;
import java.awt.image.BufferedImage;


// Interface definition for applying color effects
@FunctionalInterface
interface Effect {
    Color apply(Color color);
}

public class ImageManipulator {
    private static int x1, y1, x2, y2;
    private static int x3, y3, x4, y4;

    public static void setPolygonBounds(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        ImageManipulator.x1 = x1;
        ImageManipulator.y1 = y1;
        ImageManipulator.x2 = x2;
        ImageManipulator.y2 = y2;
        ImageManipulator.x3 = x3;
        ImageManipulator.y3 = y3;
        ImageManipulator.x4 = x4;
        ImageManipulator.y4 = y4;
    }

    public static BufferedImage fixBounds(BufferedImage image) {
        if (image == null) return null;
        BufferedImage resizedImage = new BufferedImage(Info.IMAGE_WIDTH, Info.IMAGE_HEIGHT, image.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, Info.IMAGE_WIDTH, Info.IMAGE_HEIGHT, null);
        g.dispose();
        return resizedImage;
    }

    private static void adjustPoints(int[] xPoints, int[] yPoints) {
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = Math.max(0, Math.min(xPoints[i], Info.IMAGE_WIDTH));
            yPoints[i] = Math.max(0, Math.min(yPoints[i], Info.IMAGE_HEIGHT));
        }
    }

    private static boolean pointInPolygon(int px, int py, int[] xPoints, int[] yPoints) {
        int sides = xPoints.length;
        boolean inside = false;
        for (int i = 0, j = sides - 1; i < sides; j = i++) {
            if (((yPoints[i] > py) != (yPoints[j] > py)) &&
                    (px < (xPoints[j] - xPoints[i]) * (py - yPoints[i]) / (yPoints[j] - yPoints[i]) + xPoints[i])) {
                inside = !inside;
            }
        }
        return inside;
    }

    private static void applyEffect(BufferedImage image, Effect effect) {
        int[] xPoints = {x1, x2, x3, x4};
        int[] yPoints = {y1, y2, y3, y4};
        adjustPoints(xPoints, yPoints);

        int minX = Math.min(Math.min(x1, x2), Math.min(x3, x4));
        int minY = Math.min(Math.min(y1, y2), Math.min(y3, y4));
        int maxX = Math.max(Math.max(x1, x2), Math.max(x3, x4));
        int maxY = Math.max(Math.max(y1, y2), Math.max(y3, y4));

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if (pointInPolygon(x, y, xPoints, yPoints)) {
                    int rgb = image.getRGB(x, y);
                    Color color = new Color(rgb);
                    Color newColor = effect.apply(color);
                    image.setRGB(x, y, newColor.getRGB());
                }
            }
        }
    }

    public static BufferedImage applyBlackAndWhite(BufferedImage image) {
        image = fixBounds(image);
        applyEffect(image, color -> {
            int grayValue = color.getRed();
            return grayValue < 150 ? Color.BLACK : Color.WHITE;
        });
        return image;
    }

    public static BufferedImage applyPosterize(BufferedImage image, int levels) {
        image = fixBounds(image);
        applyEffect(image, color -> {
            int red = (color.getRed() / levels) * levels;
            int green = (color.getGreen() / levels) * levels;
            int blue = (color.getBlue() / levels) * levels;
            return new Color(red, green, blue);
        });
        return image;
    }

    public static BufferedImage applySepia(BufferedImage image) {
        image = fixBounds(image);
        applyEffect(image, color -> {
            int tr = (int) (0.393 * color.getRed() + 0.769 * color.getGreen() + 0.189 * color.getBlue());
            int tg = (int) (0.349 * color.getRed() + 0.686 * color.getGreen() + 0.168 * color.getBlue());
            int tb = (int) (0.272 * color.getRed() + 0.534 * color.getGreen() + 0.131 * color.getBlue());
            int red = Math.min(255, tr);
            int green = Math.min(255, tg);
            int blue = Math.min(255, tb);
            return new Color(red, green, blue);
        });
        return image;
    }

    public static BufferedImage applyContrast(BufferedImage image, double factor) {
        image = fixBounds(image);
        applyEffect(image, color -> {
            int red = (int) Math.min(255, Math.max(0, factor * (color.getRed() - 128) + 128));
            int green = (int) Math.min(255, Math.max(0, factor * (color.getGreen() - 128) + 128));
            int blue = (int) Math.min(255, Math.max(0, factor * (color.getBlue() - 128) + 128));
            return new Color(red, green, blue);
        });
        return image;
    }

    public static BufferedImage applyNegative(BufferedImage image) {
        image = fixBounds(image);
        applyEffect(image, color -> new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()));
        return image;
    }

    public static BufferedImage applyPixelate(BufferedImage image, int pixelSize) {
        image = fixBounds(image);
        int[] xPoints = {x1, x2, x3, x4};
        int[] yPoints = {y1, y2, y3, y4};
        adjustPoints(xPoints, yPoints);

        int minX = Math.min(Math.min(x1, x2), Math.min(x3, x4));
        int minY = Math.min(Math.min(y1, y2), Math.min(y3, y4));
        int maxX = Math.max(Math.max(x1, x2), Math.max(x3, x4));
        int maxY = Math.max(Math.max(y1, y2), Math.max(y3, y4));

        for (int x = minX; x <= maxX; x += pixelSize) {
            for (int y = minY; y <= maxY; y += pixelSize) {
                if (pointInPolygon(x, y, xPoints, yPoints)) {
                    int rgb = image.getRGB(x, y);
                    for (int dx = 0; dx < pixelSize; dx++) {
                        for (int dy = 0; dy < pixelSize; dy++) {
                            int px = x + dx;
                            int py = y + dy;
                            if (px < Info.IMAGE_WIDTH && py < Info.IMAGE_HEIGHT) {
                                image.setRGB(px, py, rgb);
                            }
                        }
                    }
                }
            }
        }
        return image;
    }

    public static BufferedImage applyLighter(BufferedImage image, double factor) {
        image = fixBounds(image);
        applyEffect(image, color -> {
            int red = (int) Math.min(255, color.getRed() + factor * 255);
            int green = (int) Math.min(255, color.getGreen() + factor * 255);
            int blue = (int) Math.min(255, color.getBlue() + factor * 255);
            return new Color(red, green, blue);
        });
        return image;
    }

    public static BufferedImage applyDarker(BufferedImage image, double factor) {
        image = fixBounds(image);
        applyEffect(image, color -> {
            int red = (int) Math.max(0, color.getRed() - factor * 255);
            int green = (int) Math.max(0, color.getGreen() - factor * 255);
            int blue = (int) Math.max(0, color.getBlue() - factor * 255);
            return new Color(red, green, blue);
        });
        return image;
    }

    public static BufferedImage applyAddNoise(BufferedImage image, double noiseLevel) {
        image = fixBounds(image);
        applyEffect(image, color -> {
            int noise = (int) (Math.random() * 2 * noiseLevel - noiseLevel);
            int red = Math.min(255, Math.max(0, color.getRed() + noise));
            int green = Math.min(255, Math.max(0, color.getGreen() + noise));
            int blue = Math.min(255, Math.max(0, color.getBlue() + noise));
            return new Color(red, green, blue);
        });
        return image;
    }

    public static BufferedImage applySolarize(BufferedImage image) {
        image = fixBounds(image);
        applyEffect(image, color -> {
            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();

            red = (red > 127) ? 255 - red : red;
            green = (green > 127) ? 255 - green : green;
            blue = (blue > 127) ? 255 - blue : blue;

            return new Color(red, green, blue);
        });
        return image;
    }

    public static BufferedImage applyVintage(BufferedImage image) {
        image = applyAddNoise(image,30);
        image = applySepia(image);
        return image;
    }
}