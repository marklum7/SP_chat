import java.io.IOException;

public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection);

    void onReceiveString(TCPConnection tcpConnection, String s);

    void onDisconnect(TCPConnection tcpConnection);

    void onException(TCPConnection tcpConnection, IOException e);

}
