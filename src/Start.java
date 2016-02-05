import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Start {
    static double[][] brightness;

    public static void main(String[] args) throws IOException {
        BufferedImage img = ImageIO.read(new File("img1.png"));
        BufferedImage scaled = new BufferedImage(50, 50,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.drawImage(img, 0, 0, 50, 50, null);
        g.dispose();

        ImageIO.write(scaled, "PNG", new File("img.png"));

        img.getHeight();
        img.getWidth();
        brightness = new double[img.getHeight()][img.getWidth()];


        calculateBrightness(scaled);

        printSymbolImg(scaled);


    }

    public static void resizeImg(BufferedImage img) throws IOException {

    }

    public static void printSymbolImg(BufferedImage img) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("imgTxt.txt", "UTF-8");
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                if (brightness[i][j] < 86) {
                    System.out.print("#");
                    writer.print("#");
                }
                if (85 < brightness[i][j] && brightness[i][j] < 171) {
                    System.out.print("*");
                    writer.print("*");
                }
                if (170 < brightness[i][j]) {
                    System.out.print("'");
                    writer.print("'");
                }
            }
            System.out.println();
            writer.println();
        }
        writer.close();
    }

    public static void calculateBrightness(BufferedImage img) {
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int red = color.getRed();
                int blue = color.getBlue();
                int green = color.getGreen();
                double y = 0.3 * red + 0.59 * green + 0.11 * blue;
                brightness[i][j] = y;
            }
        }
    }
}
