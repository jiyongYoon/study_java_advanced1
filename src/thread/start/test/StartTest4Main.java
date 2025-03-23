package thread.start.test;

import static util.MyLogger.log;

public class StartTest4Main {

  public static void main(String[] args) {
    new Thread(new PrintWork("A", 1000), "Thread-A").start();
    new Thread(new PrintWork("B", 500), "Thread-B").start();
  }

  static class PrintWork implements Runnable {

    public PrintWork(String content, int sleepMs) {
      this.content = content;
      this.sleepMs = sleepMs;
    }

    private final String content;
    private final int sleepMs;

    @Override
    public void run() {
      while (true) {
        try {
          Thread.sleep(this.sleepMs);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        log(this.content);
      }
    }
  }

  /**
   * 22:34:57.885 [ Thread-B] B
   * 22:34:58.380 [ Thread-A] A
   * 22:34:58.396 [ Thread-B] B
   * 22:34:58.898 [ Thread-B] B
   * 22:34:59.391 [ Thread-A] A
   * 22:34:59.406 [ Thread-B] B
   * 22:34:59.908 [ Thread-B] B
   * 22:35:00.397 [ Thread-A] A
   * 22:35:00.412 [ Thread-B] B
   * 22:35:00.916 [ Thread-B] B
   * 22:35:01.403 [ Thread-A] A
   * 22:35:01.418 [ Thread-B] B
   * 22:35:01.921 [ Thread-B] B
   * ... 무한 실행
   */

}
