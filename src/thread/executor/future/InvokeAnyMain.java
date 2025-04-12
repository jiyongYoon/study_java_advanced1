package thread.executor.future;

import static util.MyLogger.log;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import thread.executor.CallableTask;

public class InvokeAnyMain {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    ExecutorService es = Executors.newFixedThreadPool(10);

    CallableTask task1 = new CallableTask("task1", 1000);
    CallableTask task2 = new CallableTask("task2", 2000);
    CallableTask task3 = new CallableTask("task3", 3000);

    Integer value = es.invokeAny(List.of(task1, task2, task3)); // 값 1개만 완료되어도 받기 때문에 리스트로 받지 않고 바로 값을 받음 -> 블로킹
    log("value = " + value);
    es.shutdown(); // 스레드 풀 종료 (java21에서는 es.close())
  }

  /**
   * 22:11:14.285 [pool-1-thread-1] task1 시작
   * 22:11:14.285 [pool-1-thread-2] task2 시작
   * 22:11:14.285 [pool-1-thread-3] task3 시작
   * 22:11:15.293 [pool-1-thread-1] task1 완료, return = 1000
   * 22:11:15.294 [     main] value = 1000
   * 22:11:15.294 [pool-1-thread-2] 인터럽트 발생, sleep interrupted
   * 22:11:15.295 [pool-1-thread-3] 인터럽트 발생, sleep interrupted
   */

}
