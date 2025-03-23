package thread.start;

import static util.MyLogger.log;

public class InnerRunnableMainV4 {

  public static void main(String[] args) {
    log("main() start");

    Thread thread = new Thread(() ->  log("run()"));
    thread.start();

    log("main() end");
  }

  /**
   * 22:23:24.827 [     main] main() start
   * 22:23:24.830 [     main] main() end
   * 22:23:24.830 [ Thread-0] run()
   */

}
