package thread.control.join;

import static util.MyLogger.log;

import util.ThreadUtils;

// join() 사용
public class JoinMainV4 {

  public static void main(String[] args) throws InterruptedException {
    log("start");

    SumTask task1 = new SumTask(1, 50);
    Thread thread1 = new Thread(task1, "thread-1");

    thread1.start();

    // 스레드가 종료될 때까지 대기 최대 millis 대기 - TIMED_WAITING
    log("join() - main 스레드가 thread1 종료까지 WAITING 상태로 최대 1초 대기");
    thread1.join(1000); // main 스레드는 thread1이 종료될 때까지 최대 1000ms만큼만 기다림
    log("main 스레드가 대기 완료");

    log("task1.result = " + task1.result);
  }

  /**
   * 09:08:49.947 [     main] start
   * 09:08:49.965 [     main] join() - main 스레드가 thread1 종료까지 WAITING 상태로 최대 1초 대기
   * 09:08:49.966 [ thread-1] 작업 시작
   * 09:08:50.975 [     main] main 스레드가 대기 완료
   * 09:08:50.979 [     main] task1.result = 0
   * 09:08:51.969 [ thread-1] 작업 끝, result = 1275
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
