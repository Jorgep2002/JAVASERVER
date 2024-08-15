package broadcastChat.application;

import broadcastChat.domain.ChatService;
import broadcastChat.domain.ClientHandler;
import broadcastChat.domain.SocketProcess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements SocketProcess {
	private ServerSocket serverSocket;
	private boolean running;
	private ChatService chatService;
	private static final long CLIENT_LIST_UPDATE_INTERVAL = 2000;

	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		this.running = true;
		this.chatService = new ChatService();
		startClientListUpdater();
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
				chatService.addClient(clientHandler);

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
					chatService.updateClientList();
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
				clientHandler.close();
			}
			this.serverSocket.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
