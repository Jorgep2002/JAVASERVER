package broadcastChat.infrastructure;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaServerSocket {
  private int port;
  private int amountClients;
  private String ip;

  public JavaServerSocket(String ip,int port, int amountClients) {
    this.port = port;
    this.amountClients = amountClients;
    this.ip = ip;
  }

    public ServerSocket get() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(this.ip, this.port), this.amountClients);
            return serverSocket;
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }
}
