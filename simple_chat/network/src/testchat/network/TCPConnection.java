package testchat.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by veronika on 07.01.2019.
 */
public class TCPConnection {

    private final Socket socket;
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final TCPConnectionListener eventListener;


    public TCPConnection(TCPConnectionListener eventListener, String ip, int port) throws IOException {//создает сокет
    this(eventListener, new Socket(ip, port));//вызываем второй констурктор
    }


    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {//принимает созданный сокет
        this.eventListener = eventListener;
        this.socket = socket;
        socket.getInputStream();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        rxThread = new Thread(new Runnable() { //создали анонимный класс, который реализует
                                                //Runnable, оверрайдит run().
                                                //(чтобы поток начал что-то делать ему надо передать экземпляр класса,
                                                // реализующего Runnable\создать наследника Thread\создать анонимный класс,
                                                // реализующий Runnable
            @Override
            public void run() { //слушаем входящее соединение
                try {
                    eventListener.onConnectionReady(TCPConnection.this);//передали экземпляр обрамляющего класса
                                                                                    //(если просто написать this, то передастся
                                                                                    //экземпляр анонимного класса.
                    while (!rxThread.isInterrupted()){
                        String msg = in.readLine();
                        eventListener.onReceiveString(TCPConnection.this, msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        rxThread.start();
    }

    public synchronized void sendString(String msg){
        try {
            out.write(msg + "\r\n"); // признак конца строки
            out.flush(); //в out класса BufferedWriter есть буфер, хранящий данные.
                        // Может получиться так, что они в буфер записались, но по сети не передались, flush() высвободит буфер
                        // (мы ведь хотим чтобы метод sendString(String msg) передал всю строку).
        } catch (IOException e) {
            e.printStackTrace();
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }


    public synchronized void disconnect(){
    rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            eventListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ":" + socket.getPort();//адрес с которого установлено соединение, номер порта.
    }
}
