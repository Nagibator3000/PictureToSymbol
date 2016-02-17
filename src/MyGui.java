import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MyGui {
    JFrame frame;
    JTextField textField;
      String s;

    public void goGui() {
        frame = new JFrame("Petuh");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        JPanel panel1 = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());
        JButton b1 = new JButton("From file");
        JButton b2 = new JButton("From Url");
        JButton b3 = new JButton("From id VK");
        b1.addActionListener(e -> {
            System.out.println("from file clicked");

        });

        b2.addActionListener(e -> {
            try {
                Core.setDefaultOptions();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
          String sText =  textField.getText();
            Core.setSize(sText);
           /* s= JOptionPane.showInputDialog("Enter Url");*/
           s ="https://2ch.hk/pr/thumb/642217/14545279179070s.jpg,";
            try {
                Core.urlAction(s);
            } catch (IOException e1) {
                e1.printStackTrace();
            }


            System.out.println("from url clicked");



        });
        b3.addActionListener(e -> {
            System.out.println("from id Vk clicked");
            s =JOptionPane.showInputDialog("Enter id Vk");
        });

        textField = new JTextField(10);

        JLabel label = new JLabel("Enter size");

        panel2.add(textField);
        panel2.add(b1);
        panel2.add(b2);
        panel2.add(b3);
        panel1.add(label);

        frame.add(panel1, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.CENTER);


        frame.setVisible(true);
    }




}
