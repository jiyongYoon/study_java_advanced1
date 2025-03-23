package thread.start;

import static util.MyLogger.log;

public class ManyThreadMainV1 {

  public static void main(String[] args) {
    log("main() start");

    HelloRunnable runnable = new HelloRunnable();
    Thread thread1 = new Thread(runnable);
    thread1.start();
    Thread thread2 = new Thread(runnable);
    thread2.start();
    Thread thread3 = new Thread(runnable);
    thread3.start();

    log("main() end");
  }

  /**
   * 22:12:22.903 [     main] main() start
   * 22:12:22.906 [     main] main() end
   * Thread-1: run() --> 해당 실행결과는 순서를 보장하지 않음
   * Thread-0: run() --> 해당 실행결과는 순서를 보장하지 않음
   * Thread-2: run() --> 해당 실행결과는 순서를 보장하지 않음
   */

}
