package thread.executor.future;

import static util.MyLogger.log;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import thread.executor.CallableTask;

public class InvokeAllMain {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    ExecutorService es = Executors.newFixedThreadPool(10);

    CallableTask task1 = new CallableTask("task1", 1000);
    CallableTask task2 = new CallableTask("task2", 2000);
    CallableTask task3 = new CallableTask("task3", 3000);

    List<Future<Integer>> futureList = es.invokeAll(List.of(task1, task2, task3)); // 3가지 작업 모두 끝나야 리턴됨
    for (Future<Integer> future : futureList) {
      Integer value = future.get();
      log("value = " + value);
    }
    es.shutdown(); // 스레드 풀 종료 (java21에서는 es.close())
  }

  /**
   * 22:09:46.088 [pool-1-thread-1] task1 시작
   * 22:09:46.088 [pool-1-thread-2] task2 시작
   * 22:09:46.088 [pool-1-thread-3] task3 시작
   * 22:09:47.097 [pool-1-thread-1] task1 완료, return = 1000
   * 22:09:48.101 [pool-1-thread-2] task2 완료, return = 2000
   * 22:09:49.104 [pool-1-thread-3] task3 완료, return = 3000
   * 22:09:49.105 [     main] value = 1000
   * 22:09:49.105 [     main] value = 2000
   * 22:09:49.105 [     main] value = 3000
   */

}
