package broadcastChat.infrastructure;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Session implements Runnable{
  private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	private Socket socket;

  public Session(Socket socket) {
    try {
      this.socket = socket;
      this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
      this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
    } catch (Exception e) {
      e.printStackTrace();
      this.objectOutputStream = null;
			this.objectInputStream = null;
			this.socket = null;
    }
  }

  public Object read() throws IOException, ClassNotFoundException {
    try {
      return this.objectInputStream.readObject();
    } catch (EOFException e) {
      System.out.println("Connection closed by the client.");
      throw e; // Propagar la excepción para manejarla en el nivel superior
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
      throw e; // Propagar la excepción para manejarla en el nivel superior
    }
  }
  public void write(Object data) throws IOException {
    try {
      this.objectOutputStream.writeObject(data);
      this.objectOutputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  public void close() throws IOException {
    try {
      if (this.objectOutputStream != null) {
        this.objectOutputStream.close();
      }
      if (this.objectInputStream != null) {
        this.objectInputStream.close();
      }
      if (this.socket != null) {
        this.socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw e; // Propagar la excepción para manejarla en el nivel superior
    }
  }

  @Override
  public void run() {

  }
}
