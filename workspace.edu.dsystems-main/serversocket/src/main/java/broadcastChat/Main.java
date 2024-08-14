package broadcastChat;

import broadcastChat.application.Comunicaciones;

/**
 * Hello world!
 *
 */

public class Main {
    public static void main(String[] args) {
        int port = 1802;
        int backlog = 100;
        String ip = "localhost";

        Comunicaciones comunicaciones = new Comunicaciones(ip, port, backlog);
        comunicaciones.iniciar();
    }
}