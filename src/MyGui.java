import javax.swing.*;
import java.awt.*;
import java.awt.peer.ButtonPeer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MyGui {
    JFrame frameUrl;
    JFrame mainFraim;


    public void goGui() throws IOException {
        Core.setDefaultOptions();

        mainFraim = new JFrame("MainFrame");
        mainFraim.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFraim.setSize(500, 500);
        mainFraim.setLocationRelativeTo(null);
        mainFraim.setLayout(new BorderLayout());


        JPanel panel1 = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());

        JButton b1 = new JButton("From file");
        JButton b2 = new JButton("From Url");
        JButton b3 = new JButton("From id VK");

        b1.addActionListener(e -> {
            System.out.println("from file clicked");

        });

        b2.addActionListener(e -> {
            goGuiUrl();

        });
        b3.addActionListener(e -> {
            System.out.println("from id Vk clicked");
        });


        JLabel enterText = new JLabel("Choose file source");
        panel1.add(enterText);
        panel2.add(b1);
        panel2.add(b2);
        panel2.add(b3);


        mainFraim.add(panel1, BorderLayout.NORTH);
        mainFraim.add(panel2, BorderLayout.CENTER);


        mainFraim.setVisible(true);
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

        JTextField textFieldEnterUrl = new JTextField(10);
        JTextField textFieldEnterSize = new JTextField(10);

        JButton buttonGoUrl = new JButton("Go!");
        buttonGoUrl.addActionListener(e -> {
            String size = textFieldEnterSize.getText();
            Core.setSize(size);
            try {
                Core.creatPrintWriter();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            String url = textFieldEnterUrl.getText()+",";
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
        panelUrl1.add(textFieldEnterSize);

        panelUrl2.setLayout(new FlowLayout());
        panelUrl2.add(labelUrl);
        panelUrl2.add(textFieldEnterUrl);

        panelUrl3.setLayout(new FlowLayout());
        panelUrl3.add(buttonGoUrl);

        frameUrl.add(panelUrl1,BorderLayout.NORTH);
        frameUrl.add(panelUrl2,BorderLayout.CENTER);
        frameUrl.add(panelUrl3,BorderLayout.SOUTH);

        frameUrl.setVisible(true);
    }


}
