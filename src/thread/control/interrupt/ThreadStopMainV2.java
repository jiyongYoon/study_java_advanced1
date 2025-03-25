package thread.control.interrupt;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class ThreadStopMainV2 {

  public static void main(String[] args) {
    MyTask myTask = new MyTask();
    Thread thread = new Thread(myTask, "work");
    thread.start();

    sleep(4000);
    log("작업 중단 지시 thread.interrupt()");
    thread.interrupt();
    log("work 스레드 인터럽트 상태1 = " + thread.isInterrupted()); // true, thread.interrupt()가 호출되었기 때문에
    sleep(1000);
    log("work 스레드 인터럽트 상태3 = " + thread.isInterrupted()); // false, work 스레드 인터럽트 상태2 와 동일하다.
  }

  /**
   * 09:52:16.574 [     work] 작업 중
   * 09:52:19.591 [     work] 작업 중
   * 09:52:20.568 [     main] 작업 중단 지시 thread.interrupt()
   * 09:52:20.574 [     work] work 스레드 인터럽트 상태2 = false ==> 즉각 반응 (catch 블럭에서)
   * 09:52:20.574 [     work] interrupt message = sleep interrupted
   * 09:52:20.575 [     work] state = RUNNABLE
   * 09:52:20.575 [     work] 자원 정리
   * 09:52:20.575 [     work] 작업 종료
   * 09:52:20.582 [     main] work 스레드 인터럽트 상태1 = true <-- 이 출력 순서는 보장할수 없다
   */

  static class MyTask implements Runnable {


    @Override
    public void run() {
      try {
        while (true) {
          log("작업 중");
          Thread.sleep(3000); // 이 부분이 InterruptedException 과 관련이 있는 부분임
        }
      } catch (InterruptedException e) {
        log("work 스레드 인터럽트 상태2 = " + Thread.currentThread().isInterrupted()); // false -> interrupt가 발생해서 InterruptedException이 처리될 때 다시 false로 바뀜
        log("interrupt message = " + e.getMessage());
        log("state = " + Thread.currentThread().getState());
      }
      log("자원 정리");
      log("작업 종료");
    }
  }

}
