
import java.awt.Font.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

    private static final String IP_ADDR = "localhost";
    private static final int PORT = 8189;
    private static final int WIDTH = 300;
    private static final int HEIGHT = 400;
    private static int YPOS = 0;
    static ArrayList<Integer> alax = new ArrayList<Integer>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickname = new JTextField("Пользователь");
    private final JTextField fieldInput = new JTextField();


    private TCPConnection connection;

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        try{
            Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(ClientWindow.class.getResourceAsStream("fortnitebattlefest.ttf")));
            fieldNickname.setFont(font.deriveFont(Font.BOLD, 12f));
            fieldInput.setFont(font.deriveFont(Font.BOLD, 12f));
            log.setFont(font.deriveFont(Font.BOLD, 12f));
        }
        catch(Exception e){}


        log.setSelectionColor(Color.BLACK);
        fieldNickname.setBackground(Color.ORANGE);
        fieldNickname.setSelectionColor(Color.BLACK);
        fieldNickname.setSelectedTextColor(Color.WHITE);
        log.setBackground(Color.ORANGE);
        log.setSelectedTextColor(Color.WHITE);
        add(fieldNickname, BorderLayout.NORTH);

        log.setEditable(false);
        log.setLineWrap(true);
        //add(new JScrollPane(log), BorderLayout.CENTER);

        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);

        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }
        //AddTextBlock();
        //AddTextBlock();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if (msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendString(fieldNickname.getText() + ": " + msg);
    }


    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMessage(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
    }
    public void AddTextBlock(String name) throws IOException, FontFormatException {
        String[] parts = name.split(": ");


        JLabel l = new JLabel("panel label") {

            BufferedImage  image =  ImageIO.read(new File("E:\\college\\SP\\SP_chat\\chat\\src\\user28.png"));
            Font font1 = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(ClientWindow.class.getResourceAsStream("fortnitebattlefest.ttf")));
           // Font font2 = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(ClientWindow.class.getResourceAsStream("fortnitebattlefest.ttf")));
            //ImageIcon image = new ImageIcon(ImageIO.read(new File("user.png")));
            @Override
            protected void paintComponent(Graphics g) {
                if (fieldNickname.getText().equals(parts[0])){
                    g.setColor(Color.MAGENTA);
                }else{
                    g.setColor(Color.ORANGE);
                }
                g.fillRect(1,drawpos, 283,30);
                g.setColor(Color.black);
                g.setFont(font1.deriveFont(Font.BOLD, 12f));
                g.drawString(parts[0],30, drawpos + 13);
                g.setFont(font1.deriveFont(Font.BOLD, 10f));
                g.drawString(parts[1],30, drawpos + 22);
                g.drawImage(image, 0, drawpos, this);
            }
            private int drawpos = YPOS;
        };
        l.setBounds(1,YPOS, 283,30);
        add(l);
        revalidate();
        repaint();
        YPOS = YPOS + 32;
    }
    @Override
    public void onException(TCPConnection tcpConnection, IOException e) {
        printMessage("Connection exception: " + e);
    }

    private synchronized void printMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    AddTextBlock(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (FontFormatException e) {
                    throw new RuntimeException(e);
                }
                log.append(message + "\n");
            }
        });
    }
}
