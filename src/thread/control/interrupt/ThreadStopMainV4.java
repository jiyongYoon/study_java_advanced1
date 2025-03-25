package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV4 {

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
   * 11:15:35.465 [     work] 작업 중
   * 11:15:35.465 [     work] 작업 중
   * 11:15:35.466 [     work] 작업 중
   * 11:15:35.466 [     work] 작업 중
   * 11:15:35.466 [     main] 작업 중단 지시 thread.interrupt()
   * 11:15:35.466 [     work] 작업 중
   * 11:15:35.473 [     main] work 스레드 인터럽트 상태1 = true
   * 11:15:35.473 [     work] work 스레드 인터럽트 상태2 = false <-- interrupt 상태가 false로 초기화 되었기 때문이다.
   * 11:15:35.474 [     work] 자원 정리 시도
   * 11:15:36.475 [     work] 자원 정리 완료
   * 11:15:36.475 [     work] 작업 종료
   * 11:15:36.475 [     main] work 스레드 인터럽트 상태4 = false
   */

  static class MyTask implements Runnable {


    @Override
    public void run() {
      // 인터럽트 상태 확인 및 변경 -> 인터럽트 상태면 true를 반환한 후 false로 초기화
      //                         -> 인터럽트 상태가 아니라면 false를 반환하고 상태 변경 안함
      while (!Thread.interrupted()) {
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
