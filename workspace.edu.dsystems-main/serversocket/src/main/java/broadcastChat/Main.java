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

        Comunicaciones comunicaciones = new Comunicaciones(port, backlog);
        comunicaciones.iniciar();
    }
}