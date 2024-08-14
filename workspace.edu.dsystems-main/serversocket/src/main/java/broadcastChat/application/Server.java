package broadcastChat.application;

import broadcastChat.domain.ChatService;
import broadcastChat.domain.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements SocketProcess {
	private ServerSocket serverSocket;
	private boolean running;
	private ChatService chatService; // Servicio de chat
	private static final long CLIENT_LIST_UPDATE_INTERVAL = 5000; // Intervalo en milisegundos

	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		this.running = true;
		this.chatService = new ChatService(); // Inicializar el servicio de chat
		startClientListUpdater(); // Iniciar el hilo que actualiza la lista de clientes
	}

	@Override
	public boolean bind() {
		try {
			while (running) {
				Socket clientSocket = this.serverSocket.accept();
				String clientIP = clientSocket.getInetAddress().getHostAddress();
				int clientPort = clientSocket.getPort();
				System.out.println("New connection from IP: " + clientIP + ":" + clientPort);

				ClientHandler clientHandler = new ClientHandler(clientSocket, chatService);
				chatService.addClient(clientHandler); // Agregar cliente al servicio

				Thread clientThread = new Thread(clientHandler);
				clientThread.start();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void startClientListUpdater() {
		new Thread(() -> {
			while (running) {
				try {
					Thread.sleep(CLIENT_LIST_UPDATE_INTERVAL);
					chatService.updateClientList(); // Actualizar y enviar la lista de clientes periódicamente
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public boolean close() {
		try {
			running = false;
			for (ClientHandler clientHandler : chatService.getClientHandlers()) {
				clientHandler.close(); // Cerrar cada cliente
			}
			this.serverSocket.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
