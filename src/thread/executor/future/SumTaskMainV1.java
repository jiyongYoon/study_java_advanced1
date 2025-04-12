package thread.executor.future;

import static util.MyLogger.log;

import util.ThreadUtils;

public class SumTaskMainV1 {

  public static void main(String[] args) throws InterruptedException {
    log("start");

    SumTask task1 = new SumTask(1, 50);
    SumTask task2 = new SumTask(51, 100);

    Thread thread1 = new Thread(task1, "thread-1");
    Thread thread2 = new Thread(task2, "thread-2");

    thread1.start();
    thread2.start();

    log("join() - main 스레드가 thread-1, thread-2가 종료될 때까지 대기한다.");
    thread1.join();
    thread2.join();
    log("main 스레드 대기 완료");

    log("task1.result = " + task1.result);
    log("task2.result = " + task2.result);

    int sumAll = task1.result + task2.result;
    log("task1 + task2 = " + sumAll);

    log("end");
  }

  /**
   * 22:19:56.418 [     main] start
   * 22:19:56.427 [     main] join() - main 스레드가 thread-1, thread-2가 종료될 때까지 대기한다.
   * 22:19:56.427 [ thread-2] 작업 시작
   * 22:19:56.427 [ thread-1] 작업 시작
   * 22:19:58.432 [ thread-2] 작업 끝, result = 3775
   * 22:19:58.432 [ thread-1] 작업 끝, result = 1275
   * 22:19:58.433 [     main] main 스레드 대기 완료
   * 22:19:58.433 [     main] task1.result = 1275
   * 22:19:58.433 [     main] task2.result = 3775
   * 22:19:58.433 [     main] task1 + task2 = 5050
   * 22:19:58.433 [     main] end
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
