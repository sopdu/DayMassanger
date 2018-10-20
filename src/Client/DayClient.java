package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DayClient {
    private static JTextArea textArea;
    private static JTextField textField;
    private static BufferedReader reader;  // получает сообщения о тсервера
    private static PrintWriter writer;       // отправляет сообщения на сервер
    private static String login;
    public static void main(String[] args) throws Exception {
        //login = JOptionPane.showInputDialog("Введите логнин");
        go();
    }

    private static void go() throws Exception {

        ImageIcon logo = new ImageIcon("src/logo.jpg");
        //JOptionPane.showMessageDialog(null, logo, "DayMessanger", -1);
        String textLoad = "sopduLab: DayMassenger";
        String textLoadTitle = "DayMessanger";
        JOptionPane.showMessageDialog(null, textLoad,  textLoadTitle, -1, logo);

        login = JOptionPane.showInputDialog("Введите логнин");

        JFrame frame = new JFrame("DayMassager 1.0.0");
        frame.setResizable(false); // нельзя растягивать
        frame.setLocationRelativeTo(null);  // в центре экрана
        JPanel panel = new JPanel();
        JTextArea textArea = new JTextArea(15, 30);   // размер
        textArea.setLineWrap(true);   // будет переноситься с строки на строку
        textArea.setWrapStyleWord(true);  // слов переносятся целиком
        textArea.setEnabled(false);   // нелья изменить
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JTextField text = new JTextField(20);
        JButton send = new JButton("Отправить");
        JButton reload = new JButton("Обновить");

        reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writer.print("");
            }
        });

        send.addActionListener(new send());

      //  Thread thread = new Thread(new Listener());
      //  thread.start();

        panel.add(scroll);
        panel.add(text);
        panel.add(send);
        setNet();

        Thread thread = new Thread(new Listener());
        thread.start();

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.NORTH, reload);
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static class Listener implements Runnable {
        public void run() {
            String message;
            try {
                while(
                    (message = reader.readLine()) != null
                ){
                    textArea.append(message + "\n");     // append - вставлять в конец сообщение которое получили от сервера
                }
            } catch (Exception ex){}
        }
    }

    private static class send implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = login+": "+textField.getText();
            writer.println(message);
            writer.flush(); // акрываем поток

            textArea.setText("");
            textArea.requestFocus();
        }
    }

    private static void setNet() {
        try {
            //Socket socket = new Socket("185.234.14.147", 5000);
            Socket socket = new Socket("127.0.0.1", 5000);
            //Socket socket = new Socket("localhost", 5000);
            InputStreamReader is = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(is);
            writer = new PrintWriter(socket.getOutputStream());
        } catch (Exception ex) {}
    }
}
