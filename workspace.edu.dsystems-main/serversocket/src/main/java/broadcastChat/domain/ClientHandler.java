package broadcastChat.domain;

import broadcastChat.infrastructure.Session;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientHandler implements Runnable {
    private Session session;
    private boolean running;
    private ChatService chatService;
    private final BlockingQueue<String> outgoingMessages = new LinkedBlockingQueue<>();
    private String clientName;

    public ClientHandler(Socket clientSocket, ChatService chatService) throws IOException {
        this.session = new Session(clientSocket);
        this.chatService = chatService;

        this.running = true;

        this.clientName = requestClientName();
    }

    @Override
    public void run() {
        Thread listenerThread = new Thread(this::listenForMessages);
        listenerThread.start();
        Thread writerThread = new Thread(this::processOutgoingMessages);
        writerThread.start();
        try {
            listenerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getClientName() {
        return clientName;
    }

    private String requestClientName() {
        try {
            return (String) session.read();
        } catch (IOException  e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
    private void processOutgoingMessages() {
        try {

            while (running) {
                String message = outgoingMessages.take();
                session.write(message);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
    private void listenForMessages() {
        try {
            while (running) {
                Object data = session.read();
                if (data != null) {
                    System.out.println(clientName + ": " + data);

                    chatService.broadcastMessage(clientName + ": " + data);
                }
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            outgoingMessages.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean close() throws IOException {
        try {
            running = false;
            if (session != null) {
                session.close();
            }

            if (chatService != null) {
                chatService.removeClient(this);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
