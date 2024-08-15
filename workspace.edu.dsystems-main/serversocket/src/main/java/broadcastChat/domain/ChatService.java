package broadcastChat.domain;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatService {
    private Set<ClientHandler> clientHandlers;

    public ChatService() {
        this.clientHandlers = new HashSet<>();
    }

    public synchronized void addClient(ClientHandler clientHandler) throws IOException {
        clientHandlers.add(clientHandler);
        updateClientList(); // Actualiza la lista cuando se agrega un cliente
    }

    public synchronized void removeClient(ClientHandler clientHandler) throws IOException {
        clientHandlers.remove(clientHandler);
        updateClientList(); // Actualiza la lista cuando se elimina un cliente
    }

    public synchronized void broadcastMessage(String message) throws IOException {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
    }

    public synchronized void updateClientList() throws IOException {
        List<String> clientNames = clientHandlers.stream()
                .map(ClientHandler::getClientName)
                .collect(Collectors.toList());
        broadcastMessage("CLIENT_LIST_UPDATE:" + clientNames.toString());
    }

    public Set<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }
}
