package thread.control.join;

import static util.MyLogger.log;

import java.lang.Thread.State;
import util.ThreadUtils;

// busy waiting
public class JoinMainV2_1 {

  public static void main(String[] args) {
    log("start");

    SumTask task1 = new SumTask(1, 50);
    SumTask task2 = new SumTask(51, 100);

    Thread thread1 = new Thread(task1, "thread-1");
    Thread thread2 = new Thread(task2, "thread-2");

    thread1.start();
    thread2.start();

    // CPU가 계속 작업을 하게 됨
    int count = 0;
    while (!thread1.getState().equals(State.TERMINATED)
        || !thread2.getState().equals(State.TERMINATED)) {
      if (count++ % 500_000_000 == 0) {
        log("main thread is waiting...");
      }
    }

    log("task1.result = " + task1.result);
    log("task2.result = " + task2.result);

    int sumAll = task1.result + task2.result;
    log("task1 + task2 = " + sumAll);

    log("end");
  }

  /**
   * 09:02:07.030 [     main] start
   * 09:02:07.035 [     main] main thread is waiting...
   * 09:02:07.035 [ thread-2] 작업 시작
   * 09:02:07.035 [ thread-1] 작업 시작
   * 09:02:07.630 [     main] main thread is waiting...
   * 09:02:08.275 [     main] main thread is waiting...
   * 09:02:08.898 [     main] main thread is waiting...
   * 09:02:09.054 [ thread-2] 작업 끝, result = 3775
   * 09:02:09.054 [ thread-1] 작업 끝, result = 1275
   * 09:02:09.057 [     main] task1.result = 1275
   * 09:02:09.057 [     main] task2.result = 3775
   * 09:02:09.057 [     main] task1 + task2 = 5050
   * 09:02:09.058 [     main] end
   */

  static class SumTask implements Runnable {

    int startValue;
    int endValue;
    int result = 0;

    public SumTask(int startValue, int endValue) {
      this.startValue = startValue;
      this.endValue = endValue;
    }

    @Override
    public void run() {
      log("작업 시작");
      ThreadUtils.sleep(2000);
      int sum = 0;
      for (int i = startValue; i <= endValue; i++) {
        sum += i;
      }
      result = sum;
      log("작업 끝, result = " + result);
    }
  }
}
