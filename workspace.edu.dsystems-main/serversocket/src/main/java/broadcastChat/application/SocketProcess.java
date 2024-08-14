package broadcastChat.application;

public interface SocketProcess {
  public boolean bind();
  //public List<Object> listen();
  //public boolean response(Object data);
  public boolean close();
}
