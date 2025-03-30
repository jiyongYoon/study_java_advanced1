package thread.bounded;

// 버퍼 역할
public interface BoundedQueue {
  void put(String data);

  String take();

}
