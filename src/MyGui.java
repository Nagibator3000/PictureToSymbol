import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MyGui {
    JTextField fieldSize;
    static JFrame frameUrl;
    static JFrame frameInFile;
    static JFrame frameVkId;
    JFrame mainFrame;
    static JProgressBar progressBar = new JProgressBar();
    String fileName;
    private JTextField vkIdTextField;

    public void goGui() throws IOException {
        Core.setDefaultOptions();

        mainFrame = new JFrame("MainFrame");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 150);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());


        JPanel panel1 = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());

        JButton b1 = new JButton("From file");
        JButton b2 = new JButton("From Url");
        JButton b3 = new JButton("From id VK");

        b1.addActionListener(e -> {
            goGuiInFile();
            System.out.println("from file clicked");

        });

        b2.addActionListener(e -> {
            goGuiUrl();

        });
        b3.addActionListener(e -> {
            goGuiVkId();
            System.out.println("from id Vk clicked");
        });


        JLabel enterText = new JLabel("Choose file source");
        panel1.add(enterText);
        panel2.add(b1);
        panel2.add(b2);
        panel2.add(b3);


        mainFrame.add(panel1, BorderLayout.NORTH);
        mainFrame.add(panel2, BorderLayout.CENTER);


        mainFrame.setVisible(true);
    }

    private void goGuiVkId() {
        frameVkId = new JFrame("FrameVkId");
        frameVkId.setSize(400, 200);
        frameVkId.setLocationRelativeTo(null);
        frameVkId.setLayout(new BorderLayout());

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        JLabel label = new JLabel("Enter size");
        JTextField enterSize = new JTextField(10);
        enterSize.setText(String.valueOf(Core.defaultOutImageSize));

        JLabel label1 = new JLabel("Enter vk id");
        vkIdTextField = new JTextField(10);
        vkIdTextField.setText(Core.defaultId);
        vkIdTextField.getDocument().addDocumentListener(new VkIdTextFieldListener());


        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(Core.friendsCount);

        JButton buttonGoVkAction = new JButton("Go!");
        buttonGoVkAction.addActionListener(e -> new Thread(() -> onVkGoClick(enterSize, vkIdTextField)).start());

        panel1.setLayout(new FlowLayout());
        panel1.add(label);
        panel1.add(enterSize);

        panel2.setLayout(new FlowLayout());
        panel2.add(label1);
        panel2.add(vkIdTextField);

        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("444.png"));
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            panel3.add(picLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }


        panel3.setLayout(new FlowLayout());
        panel3.add(buttonGoVkAction);
        panel3.add(buttonGoVkAction);
        panel3.add(progressBar);

        frameVkId.add(panel1, BorderLayout.NORTH);
        frameVkId.add(panel2, BorderLayout.CENTER);
        frameVkId.add(panel3, BorderLayout.SOUTH);
        frameVkId.setVisible(true);

    }

    private void onVkGoClick(JTextField enterSize, JTextField jTextFieldVkId) {
        String size = enterSize.getText();
        Core.setSize(size);
        try {
            Core.creatPrintWriter();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String userId = jTextFieldVkId.getText();
        try {
            Core.VkIdAction(userId);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Core.closePrintWriter();
    }

    private void goGuiInFile() {
        frameInFile = new JFrame();
        frameInFile.setSize(400, 200);
        frameInFile.setLocationRelativeTo(null);
        frameInFile.setLayout(new BorderLayout());

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        JLabel label = new JLabel("Enter size");
        JTextField enterSize = new JTextField(10);
        enterSize.setText(String.valueOf(Core.defaultOutImageSize));

        JButton buttonRunExploer = new JButton("Open Another");
        buttonRunExploer.addActionListener(e -> {
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                fileName = file.getName();


            }
        });

        JLabel nameFile = new JLabel("");

        JButton buttonRunActionFile = new JButton("Go!");
        buttonRunActionFile.addActionListener(e -> {
            String size = enterSize.getText();
            Core.setSize(size);
            try {
                Core.creatPrintWriter();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            try {
                Core.getImgInFile(fileName);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Core.closePrintWriter();
            frameInFile.setVisible(false);

        });

        panel1.setLayout(new FlowLayout());
        panel1.add(label);
        panel1.add(enterSize);

        panel2.setLayout(new FlowLayout());
        panel2.add(nameFile);
        panel2.add(buttonRunExploer);

        panel3.setLayout(new FlowLayout());
        panel3.add(buttonRunActionFile);

        frameInFile.add(panel1, BorderLayout.NORTH);
        frameInFile.add(panel2, BorderLayout.CENTER);
        frameInFile.add(panel3, BorderLayout.SOUTH);
        frameInFile.setVisible(true);


    }

    public void goGuiUrl() {
        frameUrl = new JFrame("FrameUrl");
        frameUrl.setSize(400, 200);
        frameUrl.setLocationRelativeTo(null);
        frameUrl.setLayout(new BorderLayout());

        JPanel panelUrl1 = new JPanel();
        JPanel panelUrl2 = new JPanel();
        JPanel panelUrl3 = new JPanel();

        JLabel labelUrlSize = new JLabel("Enter size");
        JLabel labelUrl = new JLabel("Enter Url  ");

        JTextField textUrl = new JTextField(10);
        textUrl.setText(Core.defaultIntFile);
        JTextField enterSize = new JTextField(10);
        enterSize.setText(String.valueOf(Core.defaultOutImageSize));

        JButton buttonGoUrl = new JButton("Go!");
        buttonGoUrl.addActionListener(e -> {
            String size = enterSize.getText();
            Core.setSize(size);
            try {
                Core.creatPrintWriter();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            String url = textUrl.getText() + ",";
            try {
                Core.urlAction(url);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Core.closePrintWriter();
            frameUrl.setVisible(false);

        });

        panelUrl1.setLayout(new FlowLayout());
        panelUrl1.add(labelUrlSize);
        panelUrl1.add(enterSize);

        panelUrl2.setLayout(new FlowLayout());
        panelUrl2.add(labelUrl);
        panelUrl2.add(textUrl);

        panelUrl3.setLayout(new FlowLayout());
        panelUrl3.add(buttonGoUrl);

        frameUrl.add(panelUrl1, BorderLayout.NORTH);
        frameUrl.add(panelUrl2, BorderLayout.CENTER);
        frameUrl.add(panelUrl3, BorderLayout.SOUTH);

        frameUrl.setVisible(true);
    }


    private class VkIdTextFieldListener implements DocumentListener {




        @Override
        public void insertUpdate(DocumentEvent e) {
            onChange();
        }

        private void onChange() {
            System.out.println(vkIdTextField.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
             onChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            onChange();

        }
    }
}
