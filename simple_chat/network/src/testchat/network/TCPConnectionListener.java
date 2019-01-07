package testchat.network;

/**
 * Created by veronika on 07.01.2019.
 */
public interface TCPConnectionListener {
    void onConnectionReady(TCPConnection tcpConnection);
    void onReceiveString(TCPConnection tcpConnection, String value);
    void onDisconnect(TCPConnection tcpConnection);
    void onException(TCPConnection tcpConnection, Exception ex);
}
