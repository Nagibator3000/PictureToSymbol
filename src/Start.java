import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class Start {
    static double[][] brightness;
    static BufferedImage img;
    static String config;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("config.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
               // String[] split = line.split("=");
            }
            config = sb.toString();
        } finally {
            br.close();
        }
        System.out.println(config);
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
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String urlTitel = in.readLine();
                urlTitel = urlTitel.substring(0, urlTitel.length() - 1);
                URL urlImage = new URL(urlTitel);
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
                double v = brightness[i][j];
                if (v < 86) {
                    System.out.print("#");
                    writer.print("#");
                } else if (v < 171) {
                    System.out.print("*");
                    writer.print("*");
                } else {
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
