package testchat.server;

import testchat.network.TCPConnection;
import testchat.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created by veronika on 07.01.2019.
 */
public class ChatServer implements TCPConnectionListener{ //являемся и чатсервером и слушателем

    public static void main(String[] args){
    new ChatServer();

    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer(){
    System.out.print("Server is running...");
        try {
            ServerSocket serverSocket = new ServerSocket(8189);//слушает порт, принимает входящее соединение
            while (true){  //для каждого входящего соединения создается TCPConnection. Метод accept() ждет соединения
                            // и возвращает объект сокета, когда оно устанавливается.
                            // Потом этот объект и слушатель передаются в конструктор TCPConnection(), создается экземпляр TCPConnection.
                try{
                    new TCPConnection(this, serverSocket.accept()); //как слушатель передали себя и еще передали объект сокета,
                                                                                // который при входящем соединении возвращает метод accept()
                } catch(IOException e){
                    System.out.print("TCPConnection exception" + e);
                } //ловит исключения при подключении клиентов
            }
        } catch (IOException e) {
            throw new RuntimeException(e);//если что-то пошло не так с соединением, роняем приложение.
        }
    }

    @Override//все с монитором, обращаться будет много потоков
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
    connections.add(tcpConnection);
    sendToAllConnections("Client connected: " + tcpConnection); //при конкатенации неявно вызывается toString(), который переопределен в TCPConnection.
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String msg) {
    sendToAllConnections(msg);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
    connections.remove(tcpConnection);
    sendToAllConnections("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception ex) {
    System.out.println("Exception " + ex);
    }

    private void sendToAllConnections(String msg){
    System.out.println(msg);
    for (int i=0; i < connections.size(); i++){
        connections.get(i).sendString(msg);
        }
    }
}
