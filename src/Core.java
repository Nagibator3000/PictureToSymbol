import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;


public class Core {
    static int size;
    public static String defaultMode;
    public static String defaultLocFile;
    public static String defaultIntFile;
    public static int defaultOutImageSize;
    public static String choice;
    private static String outputPath = "";
    private static PrintWriter writer;
    private static String namePic = "output";
    private static String nameFloder = "";
    public static int aLongLenght;


    public static void runConsole() throws IOException {
        setDefaultOptions();

        System.out.println("Enter size");
        String s = readString();
        setSize(s);
        System.out.println("Choose file source");
        System.out.println("Read frome file: 1");
        System.out.println("Read frome URL: 2");
        System.out.println("Read frome Vk User: 3");


        choice = readString();

        if (choice.equals("")) {
            choice = defaultMode;
        }
        creatPrintWriter();
        switch (choice) {
            case "1":
                System.out.println("Enter file name");


                String nameImg = readString();
                getImgInFile(nameImg);
                break;
            case "2":

                System.out.println("Enter Url");

                String urlTitel = readString();
                urlAction(urlTitel);
                break;
            case "3":
                System.out.println("Enter user VK id");
                String userIds = readString();
                VkIdAction(userIds);
                break;
        }
        closePrintWriter();
    }

    public static void getImgInFile(String nameImg) throws IOException {
        if (nameImg.equals("")) {
            nameImg = defaultLocFile;
        }
        BufferedImage img = ImageIO.read(new File(nameImg));
        handleImg(img);
    }

    public static void VkIdAction(String userIds) throws IOException {
        BufferedImage img;
        String jsonString = getUrl("https://api.vk.com/method/users.get?user_ids=" + userIds + "&fields=photo_max_orig");
        UsersGetResponse usersGetResponse = new Gson().fromJson(jsonString, UsersGetResponse.class);
        for (User user : usersGetResponse.response) {
            System.out.println(user.photo_max_orig);

            URL urlImage1 = new URL(user.photo_max_orig);
            img = ImageIO.read(urlImage1);
            nameFloder = user.first_name + user.last_name + "(photo)" + "/";
            namePic = user.first_name;
            handleImg(img);
        }

        String jsonString1 = getUrl("https://api.vk.com/method/friends.get?user_id=" + userIds);
        FriendsGetResponse friendsGetResponse = new Gson().fromJson(jsonString1, FriendsGetResponse.class);
        aLongLenght = friendsGetResponse.response.size();

        for (Long aLong : friendsGetResponse.response) {
            jsonString = getUrl("https://api.vk.com/method/users.get?user_ids=" + aLong + "&fields=photo_max_orig");
            usersGetResponse = new Gson().fromJson(jsonString, UsersGetResponse.class);
            MyGui.progressBar.setValue(friendsGetResponse.response.indexOf(aLong));
            for (User user : usersGetResponse.response) {
                System.out.println(user.photo_max_orig);
                System.out.println(user.first_name);

                URL urlImage1 = new URL(user.photo_max_orig);
                img = ImageIO.read(urlImage1);
                namePic = user.first_name + "_" + user.last_name;

                handleImg(img);
                writer.println(user.first_name);
            }
        }
    }

    public static void closePrintWriter() {
        writer.close();
    }

    public static void creatPrintWriter() throws FileNotFoundException, UnsupportedEncodingException {
        writer = new PrintWriter(outputPath + "imgTxt.txt", "UTF-8");
    }

    public static void urlAction(String urlTitel) throws IOException {
        BufferedImage img;
        if (urlTitel.equals("")) {
            urlTitel = defaultIntFile;
        } else {
            urlTitel = urlTitel.substring(0, urlTitel.length() - 1);
        }
        URL urlImage = new URL(urlTitel);
        img = ImageIO.read(urlImage);
        handleImg(img);
    }


    public static void setDefaultOptions() throws IOException {
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
    }

    public static void setSize(String s) {
        if (s.equals("")) {
            size = defaultOutImageSize;
        } else {
            size = Integer.parseInt(s);
        }
    }

    public static void handleImg(BufferedImage img) throws IOException {
        BufferedImage scaled = resizeImg(img);
        String[][] symbols = new String[scaled.getHeight()][scaled.getWidth()];
        double[][] brightness = new double[scaled.getHeight()][scaled.getWidth()];
        calculateBrightness(scaled, brightness);
        printSymbolImg(scaled, brightness, symbols);
        int fontSize = 12;

        printColorInFile(scaled, symbols, fontSize);


    }

    public static void printColorInFile(BufferedImage scaled, String[][] symbols, int fontSize) throws IOException {
        BufferedImage outputImg =
                new BufferedImage(scaled.getWidth() * 7, scaled.getHeight() * 10,
                        BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outputImg.createGraphics();
        int h = 0;
        int h2 = 1;
        for (int i = 0; i < scaled.getHeight(); i++) {
            for (int j = 0; j < scaled.getWidth(); j++) {
                Color color = new Color(scaled.getRGB(j, i));

                g2.setColor(color);
                g2.drawString(symbols[i][j], h, h2 + 8);
                h = h + 7;
            }
            h = 0;
            h2 = h2 + 10;

        }

        File myPath = new File("output/" + nameFloder);
        myPath.mkdir();
        File outputFile = new File("output/" + nameFloder + namePic + ".png");
        ImageIO.write(outputImg, "png", outputFile);
        System.out.println("saved output outputImage " + outputFile);
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
        float ratio = img.getHeight() / (img.getWidth() * 1f);
        int height = (int) (size * ratio);
        BufferedImage scaled = new BufferedImage(size, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.drawImage(img, 0, 0, size, (int) (size * ratio), null);
        g.dispose();
        return scaled;
    }

    public static void printSymbolImg(BufferedImage img, double[][] brightness, String[][] symbol) throws FileNotFoundException, UnsupportedEncodingException {

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                double v = brightness[i][j];
                if (v < 86) {
                    symbol[i][j] = "#";
                    //   System.out.print("#");
                    writer.print("#");
                } else if (v < 171) {
                    symbol[i][j] = "&";
                    //    System.out.print("#");
                    writer.print("*");
                } else {
                    symbol[i][j] = "3";
                    //  System.out.print("*");
                    writer.print("'");
                }
            }
            System.out.println();
            writer.println();
        }

    }

    public static void calculateBrightness(BufferedImage img, double[][] brightness) {
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                double y = 0.3 * red + 0.59 * green + 0.11 * blue;

                brightness[i][j] = y;
            }
        }
    }


}
