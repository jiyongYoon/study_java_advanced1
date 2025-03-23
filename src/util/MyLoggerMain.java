package util;

import static util.MyLogger.log;

public class MyLoggerMain {

  public static void main(String[] args) {
    log("hello thread");
    log(123);
  }

  /**
   * 22:09:46.056 [     main] hello thread
   * 22:09:46.059 [     main] 123
   */

}
