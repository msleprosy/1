package testchat.client;

import testchat.network.TCPConnection;
import testchat.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by veronika on 07.01.2019.
 */
public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

    private static final String IP_ADDR = "192.168.1.5";
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    public static void main(String[] args){
    SwingUtilities.invokeLater(new Runnable() { //к окну интерфейса можно обращаться только через специальный поток
        @Override
        public void run() {
            new ClientWindow();
        }
    });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickname = new JTextField("jiffy.of.eternity");
    private final JTextField fieldInput = new JTextField();

    private TCPConnection connection;

  private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);//посередине
    log.setEditable(false);
    log.setLineWrap(true);
    add(log, BorderLayout.CENTER); //поле log, в котором все сообщения чата - в центре
    fieldInput.addActionListener(this); //устанавливаем на поле ActionListener;
                                         // передаем экземпляр класса ClientWindow, имплементирующего ActionListener.
                                        // (всё это чтобы перехватывать нажатие энтер в поле ввода)
    add(fieldInput, BorderLayout.SOUTH);
    add(fieldNickname, BorderLayout.NORTH);
    Font fontLog = new Font("Helvetica", Font.PLAIN, 14);
        log.setFont(fontLog);
    Font fontInput = new Font("Helvetica", Font.PLAIN, 14);
        fieldInput.setFont(fontInput);
    Font fontNickname = new Font("Helvetica", Font.BOLD, 14); 
        fieldNickname.setFont(fontNickname);
        fieldNickname.setForeground(Color.GRAY);
        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException ex) {
            printMsg("Exception " + ex);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) { //по нажатию энтер передать сообщение
    String msg = fieldInput.getText();
    if (msg.equals("")) return;
    fieldInput.setText(null);
    connection.sendString(fieldNickname.getText() + " : " + msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
    printMsg("Connection is ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String msg) {
     printMsg(msg);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
     printMsg("Connection is closed...");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception ex) {
    printMsg("Exception " + ex);
    }

    private synchronized void printMsg(String msg){ //обращение происходит как из потока соединения, так и из потока окна, поэтому монитор
    SwingUtilities.invokeLater(new Runnable() {// к printMsg(String msg) будут обращаться два потока;
                                                // этот специальный поток позволяет описывать логику работы с окном свинга
        @Override
        public void run() {
        log.append(msg + "\n");
        log.setCaretPosition(log.getDocument().getLength());//автоскролл, каретка устанавливается в конец документа
        }
    });
    }
}
