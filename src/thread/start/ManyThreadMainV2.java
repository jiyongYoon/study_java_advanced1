package thread.start;

import static util.MyLogger.log;

public class ManyThreadMainV2 {

  public static void main(String[] args) {
    log("main() start");

    HelloRunnable runnable = new HelloRunnable();
    for (int i = 0; i < 10; i++) {
      Thread thread = new Thread(runnable);
      thread.start();
    }

    log("main() end");
  }

  /**
   * 22:15:04.573 [     main] main() start
   * 22:15:04.577 [     main] main() end
   * Thread-3: run()
   * Thread-7: run()
   * Thread-8: run()
   * Thread-9: run()
   * Thread-2: run()
   * Thread-0: run()
   * Thread-1: run()
   * Thread-4: run()
   * Thread-5: run()
   * Thread-6: run()
   */

}
