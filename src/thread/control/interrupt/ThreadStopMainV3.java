package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV3 {

  public static void main(String[] args) {
    MyTask myTask = new MyTask();
    Thread thread = new Thread(myTask, "work");
    thread.start();

    sleep(30);
    log("작업 중단 지시 thread.interrupt()");
    thread.interrupt();
    log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted()); // true, thread.interrupt()가 호출되었기 때문에
    sleep(1000);
    log("work 스레드 인터럽트 상태4 = " + thread.isInterrupted()); // false, work 스레드 인터럽트 상태2 와 동일하다.
  }

  /**
   * 11:11:22.562 [     work] 작업 중
   * 11:11:22.562 [     work] 작업 중
   * 11:11:22.562 [     work] 작업 중
   * 11:11:22.562 [     main] 작업 중단 지시 thread.interrupt()
   * 11:11:22.562 [     work] 작업 중
   * 11:11:22.569 [     work] work 스레드 인터럽트 상태2 = true
   * 11:11:22.569 [     main] work 스레드 인터럽트 상태1 = true
   * 11:11:22.569 [     work] 자원 정리 시도
   * 11:11:22.569 [     work] 자원 정리 실패 - 자원 정리 중 interrupt 발생 ==> 사실 interrupt가 발생한게 아니라, 기존 interrupt 상태가 그대로 유지되었기 때문에 catch 블럭으로 들어오게 됨
   * 11:11:22.569 [     work] work 스레드 인터럽트 상태3 = false
   * 11:11:22.569 [     work] 작업 종료
   * 11:11:23.583 [     main] work 스레드 인터럽트 상태4 = false
   */

  static class MyTask implements Runnable {


    @Override
    public void run() {
      // 인터럽트 상태 확인
      while (!Thread.currentThread().isInterrupted()) {
        log("작업 중");
      }
      log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted()); // true -> interrupt가 발생

      try {
        log("자원 정리 시도");
        Thread.sleep(1000);
        log("자원 정리 완료");
      } catch (InterruptedException e) {
        log("자원 정리 실패 - 자원 정리 중 interrupt 발생 ==> 사실 interrupt가 발생한게 아니라, 기존 interrupt 상태가 그대로 유지되었기 때문에 catch 블럭으로 들어오게 됨");
        log("work 스레드 인터럽트 상태3 = " + Thread.currentThread().isInterrupted()); // false -> InterruptedException 발생하면서 인터럽트 상태가 초기화
      }
      log("작업 종료");
    }
  }

}
