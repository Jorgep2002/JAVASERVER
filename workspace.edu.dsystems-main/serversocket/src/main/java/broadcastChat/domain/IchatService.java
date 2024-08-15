package broadcastChat.domain;


import java.io.IOException;
import java.util.Set;

public interface IchatService {
    void addClient(ClientHandler clientHandler) throws IOException;
    void removeClient(ClientHandler clientHandler) throws IOException;
    void broadcastMessage(String message) throws IOException;
    void updateClientList() throws IOException;
    Set<ClientHandler> getClientHandlers();
}