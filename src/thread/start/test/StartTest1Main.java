package thread.start.test;

import static util.MyLogger.log;

public class StartTest1Main {

  public static void main(String[] args) {
    CounterThread counterThread = new CounterThread();
    counterThread.start();
  }

  /**
   * 22:26:36.878 [ Thread-0] value: 1
   * 22:26:37.893 [ Thread-0] value: 2
   * 22:26:38.903 [ Thread-0] value: 3
   * 22:26:39.909 [ Thread-0] value: 4
   * 22:26:40.914 [ Thread-0] value: 5
   */


  static class CounterThread extends Thread {

    @Override
    public void run() {
      for (int i = 0; i < 5; i++) {
        log("value: " + (i + 1));
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

}
