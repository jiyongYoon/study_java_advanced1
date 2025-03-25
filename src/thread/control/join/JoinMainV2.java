package thread.control.join;

import static util.MyLogger.log;

import util.ThreadUtils;

// sleep() 사용
public class JoinMainV2 {

  public static void main(String[] args) {
    log("start");

    SumTask task1 = new SumTask(1, 50);
    SumTask task2 = new SumTask(51, 100);

    Thread thread1 = new Thread(task1, "thread-1");
    Thread thread2 = new Thread(task2, "thread-2");

    thread1.start();
    thread2.start();

    // 딱 알맞은 타이밍을 맞추어 기다리기는 어려움
    log("main 스레드 sleep()");
    ThreadUtils.sleep(3000);
    log("main 스레드 깨어남");

    log("task1.result = " + task1.result);
    log("task2.result = " + task2.result);

    int sumAll = task1.result + task2.result;
    log("task1 + task2 = " + sumAll);

    log("end");
  }

  /**
   * 08:55:29.997 [     main] start
   * 08:55:30.015 [     main] main 스레드 sleep()
   * 08:55:30.015 [ thread-2] 작업 시작
   * 08:55:30.015 [ thread-1] 작업 시작
   * 08:55:32.022 [ thread-2] 작업 끝, result = 3775
   * 08:55:32.022 [ thread-1] 작업 끝, result = 1275
   * 08:55:33.025 [     main] main 스레드 깨어남
   * 08:55:33.025 [     main] task1.result = 1275
   * 08:55:33.026 [     main] task2.result = 3775
   * 08:55:33.026 [     main] task1 + task2 = 5050
   * 08:55:33.026 [     main] end
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
