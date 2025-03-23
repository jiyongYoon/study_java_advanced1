package thread.start;

import static util.MyLogger.log;

public class InnerRunnableMainV1 {

  public static void main(String[] args) {
    log("main() start");

    MyRunnable runnable = new MyRunnable();
    Thread thread = new Thread(runnable);
    thread.start();

    log("main() end");
  }

  /**
   * 22:19:13.107 [     main] main() start
   * 22:19:13.116 [     main] main() end
   * 22:19:13.116 [ Thread-0] run()
   */

  static class MyRunnable implements Runnable {

    @Override
    public void run() {
      log("run()");
    }
  }

}
