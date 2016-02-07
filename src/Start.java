import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;


public class Start {
    static double[][] brightness;
    static BufferedImage img;
    static int size;
    public static String defaultMode;
    public static String defaultLocFile;
    public static String defaultIntFile;
    public static int defaultOutImageSize;
    public static String choice;
    private static String outputPath = "";

    public static void main(String[] args) throws IOException {

        String userIds = "73860786,1";
        String jsonString = getUrl("https://api.vk.com/method/users.get?user_ids=" + userIds + "&fields=photo_max_orig");

        Container container = new Gson().fromJson(jsonString, Container.class);
        for (User user : container.response) {
            System.out.println(user.photo_max_orig);

        }
        BufferedReader br = new BufferedReader(new FileReader("config.txt"));
        try {

            String line = br.readLine();

            while (line != null) {
                String[] split = line.split("=");
                switch (split[0]) {
                    case "default_mode":
                        defaultMode = split[1];
                        break;
                    case "default_local_file":
                        defaultLocFile = split[1];
                        break;
                    case "default_internet_file":
                        defaultIntFile = split[1];
                        break;
                    case "default_output_image_size":
                        defaultOutImageSize = Integer.parseInt(split[1]);
                        break;
                }

                line = br.readLine();

            }

        } finally {
            br.close();
        }

        System.out.println("Choose file source");
        System.out.println("Read frome file: 1");
        System.out.println("Read frome URL: 2");
        System.out.println("Read frome Vk User: 3");

        choice = readString();

        if (choice.equals("")) {
            choice = defaultMode;
        }
        switch (choice) {
            case "1":
                System.out.println("Enter file name");


                String nameImg = readString();
                if (nameImg.equals("")) {
                    nameImg = defaultLocFile;
                }
                img = ImageIO.read(new File(nameImg));
                break;
            case "2":

                System.out.println("Enter Url");

                String urlTitel = readString();
                if (urlTitel.equals("")) {
                    urlTitel = defaultIntFile;
                } else {
                    urlTitel = urlTitel.substring(0, urlTitel.length() - 1);
                }
                URL urlImage = new URL(urlTitel);
                img = ImageIO.read(urlImage);
                break;
            case "3":
        }
        System.out.println("Enter size");
        String s = readString();
        if (s.equals("")) {
            size = defaultOutImageSize;
        } else {
            size = Integer.parseInt(s);
        }

        BufferedImage scaled = resizeImg(img);


        img.getHeight();
        img.getWidth();
        brightness = new double[img.getHeight()][img.getWidth()];


        calculateBrightness(scaled);

        printSymbolImg(scaled);


    }

    public static String getUrl(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static String readString() throws IOException {
        BufferedReader bufr = new BufferedReader(new InputStreamReader(System.in));
        return bufr.readLine();
    }

    public static BufferedImage resizeImg(BufferedImage img) {
        BufferedImage scaled = new BufferedImage(size, size,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.drawImage(img, 0, 0, size, size, null);
        g.dispose();
        return scaled;
    }

    public static void printSymbolImg(BufferedImage img) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(outputPath + "imgTxt.txt", "UTF-8");
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
/*  BufferedImage map = ImageIO.read(new File("map.png"));

        BufferedImage outputImage =
                new BufferedImage(WIDTH, HEIGHT,
                        BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = outputImage.createGraphics();
из g2 можно вызывать методы установки цвета, рисование символо, строк
        g2.drawImage(map, 0, 0, WIDTH, HEIGHT, (img, f, x1, y1, width, height) -> true);



        g2.setStroke(new BasicStroke(3));


        File outputFile = new File("output.png");
        ImageIO.write(outputImage, "png", outputFile);
        System.out.println("saved output outputImage " + outputFile);
    }*/