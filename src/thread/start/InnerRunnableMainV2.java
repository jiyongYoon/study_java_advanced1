package thread.start;

import static util.MyLogger.log;

public class InnerRunnableMainV2 {

  public static void main(String[] args) {
    log("main() start");

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        log("run()");
      }
    };

    Thread thread = new Thread(runnable);
    thread.start();

    log("main() end");
  }

  /**
   * 22:20:59.448 [     main] main() start
   * 22:20:59.457 [     main] main() end
   * 22:20:59.457 [ Thread-0] run()
   */
}
