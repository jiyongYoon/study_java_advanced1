package thread.control.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class NoVolatileFlagMain {

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
   * 16:09:11.802 [     main] runFlag = true
   * 16:09:11.805 [     work] task 시작
   * 16:09:12.822 [     main] runFlag를 false로 변경 시도
   * 16:09:12.822 [     main] runFlag = false // 변경은 잘 됐으나
   * 16:09:12.823 [     main] main 종료
   * ... 그러나 프로그램은 계속 진행중 -> 즉, work 스레드가 종료되지 않고 계속 while 을 돌고 있음
   */

  static class MyTask implements Runnable {

    boolean runFlag = true;
//    volatile boolean runFlag = true;

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
