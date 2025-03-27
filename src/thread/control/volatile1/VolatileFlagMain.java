package thread.control.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileFlagMain {

  public static void main(String[] args) {
    MyTask myTask = new MyTask();
    Thread thread = new Thread(myTask, "work");
    log("runFlag = " + myTask.runFlag);
    thread.start();

    sleep(1000);
    log("runFlag를 false로 변경 시도");
    myTask.runFlag = false;
    log("runFlag = " + myTask.runFlag);
    log("main 종료");
  }

  /**
   * 16:23:59.262 [     main] runFlag = true
   * 16:23:59.266 [     work] task 시작
   * 16:24:00.273 [     main] runFlag를 false로 변경 시도
   * 16:24:00.273 [     work] task 종료
   * 16:24:00.274 [     main] runFlag = false
   * 16:24:00.274 [     main] main 종료
   */

  static class MyTask implements Runnable {

//    boolean runFlag = true;
    volatile boolean runFlag = true;

    @Override
    public void run() {
      while (runFlag) {
        log("task 시작");
        while (runFlag) {
          // runFlag가 false로 변하면 탈출
        }
        log("task 종료");
      }
    }
  }
}
