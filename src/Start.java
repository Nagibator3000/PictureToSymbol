import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class Start {
    static double[][] brightness;
    static BufferedImage img;

    public static void main(String[] args) throws IOException {
        System.out.println("Choose file source");
        System.out.println("Read frome file: 1");
        System.out.println("Read frome URL: 2");
        Scanner scn = new Scanner(System.in);
        int choise = scn.nextInt();
        switch (choise) {
            case 1:
                System.out.println("Enter file name");
                Scanner scanner = new Scanner(System.in);
                String nameImg = scanner.next();
                img = ImageIO.read(new File(nameImg + ".png"));
                break;
            case 2:
                System.out.println("Enter Url");
                Scanner scanner1 = new Scanner(System.in);
                String urlTitle = scanner1.next();
                URL urlImage = new URL(urlTitle);
                img = ImageIO.read(urlImage);
                break;
        }


        BufferedImage scaled = resizeImg(img);


        img.getHeight();
        img.getWidth();
        brightness = new double[img.getHeight()][img.getWidth()];


        calculateBrightness(scaled);

        printSymbolImg(scaled);


    }

    public static BufferedImage resizeImg(BufferedImage img) {
        BufferedImage scaled = new BufferedImage(100, 100,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.drawImage(img, 0, 0, 100, 100, null);
        g.dispose();
        return scaled;
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
