package thread.start.test;

import static util.MyLogger.log;

public class StartTest2Main {

  public static void main(String[] args) {
    Thread thread = new Thread(new CounterRunnable(), "counter");
    thread.start();
  }

  /**
   * 22:29:28.756 [  counter] value: 1
   * 22:29:29.763 [  counter] value: 2
   * 22:29:30.769 [  counter] value: 3
   * 22:29:31.774 [  counter] value: 4
   * 22:29:32.784 [  counter] value: 5
   */

  static class CounterRunnable implements Runnable {

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
