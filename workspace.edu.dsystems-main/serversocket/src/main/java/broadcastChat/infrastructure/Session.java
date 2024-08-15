package broadcastChat.infrastructure;

import java.io.EOFException;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;

public class Session {
  private DataOutputStream dataOutputStream;
  private DataInputStream dataInputStream;
	private Socket socket;

  public Session(Socket socket) {
    try {
      this.socket = socket;
      this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
      this.dataInputStream = new DataInputStream(this.socket.getInputStream());
    } catch (Exception e) {
      e.printStackTrace();
      this.dataOutputStream = null;
			this.dataInputStream = null;
			this.socket = null;
    }
  }

  public Object read() throws IOException {
    try {
      return this.dataInputStream.readUTF();
    } catch (EOFException e) {
      System.out.println("Connection closed by the client.");
      throw e; // Propagar la excepción para manejarla en el nivel superior
    } catch (IOException e) {
      e.printStackTrace();
      throw e; // Propagar la excepción para manejarla en el nivel superior
    }
  }
  public void write(String data) throws IOException {
    try {
      this.dataOutputStream.writeUTF(data);
      this.dataOutputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  public void close() throws IOException {
    try {
      if (this.dataOutputStream != null) {
        this.dataOutputStream.close();
      }
      if (this.dataInputStream != null) {
        this.dataInputStream.close();
      }
      if (this.socket != null) {
        this.socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }


}
