package thread.executor.future;

import static util.MyLogger.log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SumTaskMainV2 {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    SumTask task1 = new SumTask(1, 50);
    SumTask task2 = new SumTask(51, 100);

    ExecutorService es = Executors.newFixedThreadPool(2);

    Future<Integer> future1 = es.submit(task1); // 여기는 블로킹 아님
    Future<Integer> future2 = es.submit(task2); // 여기는 블로킹 아님

    Integer sum1 = future1.get(); // 여기서 블로킹
    Integer sum2 = future2.get(); // 여기서 블로킹

    log("task1.result = " + sum1);
    log("task2.result = " + sum2);

    int sumAll = sum1 + sum2;
    log("task1 + task2 = " + sumAll);
    log("end");

    es.shutdown(); // 스레드 풀 종료 (java21에서는 es.close())
  }

  /**
   * 22:23:52.932 [pool-1-thread-2] 작업 시작
   * 22:23:52.932 [pool-1-thread-1] 작업 시작
   * 22:23:54.938 [pool-1-thread-1] 작업 완료, result = 1275
   * 22:23:54.938 [pool-1-thread-2] 작업 완료, result = 3775
   * 22:23:54.938 [     main] task1.result = 1275
   * 22:23:54.939 [     main] task2.result = 3775
   * 22:23:54.939 [     main] task1 + task2 = 5050
   * 22:23:54.939 [     main] end
   */

  static class SumTask implements Callable<Integer> {
    int startValue;
    int endValue;

    public SumTask(int startValue, int endValue) {
      this.startValue = startValue;
      this.endValue = endValue;
    }

    @Override
    public Integer call() throws InterruptedException { // Runnable과 다르게 체크 예외 받을 수 있음
      log("작업 시작");
      Thread.sleep(2000); // 2초 대기
      int result = 0;
      for (int i = startValue; i <= endValue; i++) {
        result += i;
      }
      log("작업 완료, result = " + result);
      return result;
    }
  }

}
