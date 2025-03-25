package thread.control.join;

import static util.MyLogger.log;

import util.ThreadUtils;

// join() 사용
public class JoinMainV3 {

  public static void main(String[] args) throws InterruptedException {
    log("start");

    SumTask task1 = new SumTask(1, 50);
    SumTask task2 = new SumTask(51, 100);

    Thread thread1 = new Thread(task1, "thread-1");
    Thread thread2 = new Thread(task2, "thread-2");

    thread1.start();
    thread2.start();

    // 스레드가 종료될 때까지 대기 - WAITING 상태로
    log("join() - main 스레드가 thread1, thread2 종료까지 WAITING 상태로 대기");
    thread1.join(); // main 스레드는 thread1이 종료될 때까지 기다림
    thread2.join(); // main 스레드는 thread2이 종료될 때까지 기다림
    log("main 스레드가 대기 완료");

    log("task1.result = " + task1.result);
    log("task2.result = " + task2.result);

    int sumAll = task1.result + task2.result;
    log("task1 + task2 = " + sumAll);

    log("end");
  }

  /**
   * 09:04:24.632 [     main] start
   * 09:04:24.637 [     main] join() - main 스레드가 thread1, thread2 종료까지 WAITING 상태로 대기
   * 09:04:24.637 [ thread-1] 작업 시작
   * 09:04:24.637 [ thread-2] 작업 시작
   * 09:04:26.651 [ thread-2] 작업 끝, result = 3775
   * 09:04:26.651 [ thread-1] 작업 끝, result = 1275
   * 09:04:26.651 [     main] main 스레드가 대기 완료
   * 09:04:26.652 [     main] task1.result = 1275
   * 09:04:26.652 [     main] task2.result = 3775
   * 09:04:26.652 [     main] task1 + task2 = 5050
   * 09:04:26.653 [     main] end
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
