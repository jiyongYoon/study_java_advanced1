package thread.start.test;

import static util.MyLogger.log;

public class StartTest3Main {

  public static void main(String[] args) {
    Thread thread = new Thread(new Runnable() {
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
    }, "counter");
    thread.start();
  }

  /**
   * 22:30:46.143 [  counter] value: 1
   * 22:30:47.148 [  counter] value: 2
   * 22:30:48.161 [  counter] value: 3
   * 22:30:49.169 [  counter] value: 4
   * 22:30:50.180 [  counter] value: 5
   */

}
