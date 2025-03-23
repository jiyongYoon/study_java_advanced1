package thread.start;

import static util.MyLogger.log;

public class InnerRunnableMainV3 {

  public static void main(String[] args) {
    log("main() start");

    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        log("run()");
      }
    });
    thread.start();

    log("main() end");
  }

  /**
   * 22:22:19.130 [     main] main() start
   * 22:22:19.138 [     main] main() end
   * 22:22:19.138 [ Thread-0] run()
   */
}
