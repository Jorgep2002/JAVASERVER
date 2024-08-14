package broadcastChat.application;

import java.net.ServerSocket;

import broadcastChat.infrastructure.JavaServerSocket;

public class Comunicaciones {

    private int port;
    private int backlog;

    public Comunicaciones(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;
    }

    public void iniciar() {
        System.out.println("Java Server Socket");

        JavaServerSocket javaServerSocket = new JavaServerSocket(port, backlog);
        ServerSocket serverSocket = javaServerSocket.get();
        if (serverSocket == null) {
            System.out.println("ServerSocket is null");
            return;
        }

        Server server = new Server(serverSocket);

        System.out.println("Server is running and waiting for connections...");

        while (true) {
            if (!server.bind()) {
                System.out.println("Server bind failed");
                continue;
            }

            // Esperar un tiempo antes de reiniciar el servidor (opcional)
            try {
                Thread.sleep(1000); // Espera de 1 segundo (ajustar según sea necesario)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Reiniciar el servidor si es necesario
            System.out.println("Reiniciando el servidor...");
            if (!server.close()) {
                System.out.println("Server close failed");
            } else {
                System.out.println("Server closed, waiting for new connections...");
            }

            // Reiniciar el servidor con una nueva instancia de ServerSocket
            serverSocket = javaServerSocket.get();
            if (serverSocket == null) {
                System.out.println("ServerSocket is null after restart");
                return;
            }

        }
    }
}