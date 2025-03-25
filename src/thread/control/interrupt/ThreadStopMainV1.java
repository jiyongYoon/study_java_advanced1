package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.*;

public class ThreadStopMainV1 {

  public static void main(String[] args) {
    MyTask myTask = new MyTask();
    Thread thread = new Thread(myTask, "work");
    thread.start();

    sleep(4000);
    log("작업 중단 지시 runFlag = false");
    myTask.runFlag = false;
    log("작업 중단 지시 완료");
  }

  /**
   * 09:34:49.088 [     work] 작업 중
   * 09:34:52.098 [     work] 작업 중
   * 09:34:53.078 [     main] 작업 중단 지시 runFlag = false
   * 09:34:53.078 [     main] 작업 중단 지시 완료
   * 09:34:55.099 [     work] 자원 정리 // 약 2초 경과 후 실행 (work thread가 자고 있으니)
   * 09:34:55.099 [     work] 작업 종료
   */

  static class MyTask implements Runnable {

    volatile boolean runFlag = true; // 여러 스레드에서 공유하는 값에 사용하는 키워드 (힙영역에 올라감)

    @Override
    public void run() {
      while (runFlag) {
        log("작업 중");
        sleep(3000);
      }
      log("자원 정리");
      log("작업 종료");
    }
  }

}
