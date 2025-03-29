package thread.sync.lock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.locks.LockSupport;

public class LockSupportMainV2 {

  public static void main(String[] args) {
    Thread thread1 = new Thread(new ParkTask(), "Thread-1");
    thread1.start();
    // 잠시 대기하여 Thread-1이 park 상태에 빠질 시간을 준다.
    sleep(100);

    log("Thread-1 state: " + thread1.getState());
  }

  /**
   * 22:15:55.274 [ Thread-1] park 시작
   * 22:15:55.373 [     main] Thread-1 state: TIMED_WAITING
   * 22:15:57.285 [ Thread-1] park 종료, state: RUNNABLE
   * 22:15:57.288 [ Thread-1] 인터럽트 상태: false
   */

  static class ParkTask implements Runnable {

    @Override
    public void run() {
      log("park 시작");
      LockSupport.parkNanos(2_000_000_000); // 2초 뒤
      log("park 종료, state: " + Thread.currentThread().getState());
      log("인터럽트 상태: " + Thread.currentThread().isInterrupted());
    }
  }

}
