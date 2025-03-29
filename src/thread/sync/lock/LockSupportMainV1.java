package thread.sync.lock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.locks.LockSupport;

public class LockSupportMainV1 {

  public static void main(String[] args) {
    Thread thread1 = new Thread(new ParkTask(), "Thread-1");
    thread1.start();
    // 잠시 대기하여 Thread-1이 park 상태에 빠질 시간을 준다.
    sleep(100);

    log("Thread-1 state: " + thread1.getState());

    // 1. unpark 사용
    log("main -> unpart(Thread-1)");
    LockSupport.unpark(thread1);
    /**
     * 22:14:27.426 [ Thread-1] park 시작
     * 22:14:27.522 [     main] Thread-1 state: WAITING
     * 22:14:27.523 [     main] main -> unpart(Thread-1)
     * 22:14:27.523 [ Thread-1] park 종료, state: RUNNABLE
     * 22:14:27.526 [ Thread-1] 인터럽트 상태: false           <-- 차이
     */

    // 2. interrupt() 사용
//    log("main -> thread1.interrupt()");
//    thread1.interrupt();
    /**
     * 22:13:56.475 [ Thread-1] park 시작
     * 22:13:56.578 [     main] Thread-1 state: WAITING
     * 22:13:56.578 [     main] main -> thread1.interrupt()
     * 22:13:56.579 [ Thread-1] park 종료, state: RUNNABLE
     * 22:13:56.582 [ Thread-1] 인터럽트 상태: true            <-- 차이
     */
  }

  static class ParkTask implements Runnable {

    @Override
    public void run() {
      log("park 시작");
      LockSupport.park();
      log("park 종료, state: " + Thread.currentThread().getState());
      log("인터럽트 상태: " + Thread.currentThread().isInterrupted());
    }
  }

}
