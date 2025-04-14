package thread.executor.reject;

import static util.MyLogger.log;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;
import thread.executor.RunnableTask;

public class RejectMainV1 {

  public static void main(String[] args) {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
        1,
        1,
        0,
        TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        new AbortPolicy() // 기본 정책
//        new ThreadPoolExecutor.DiscardPolicy() // 작업 버림
//        new ThreadPoolExecutor.CallerRunsPolicy() // 작업을 제출한 스레드가 대신 작업을 직접 수행
    );

    executor.submit(new RunnableTask("task1"));

    try {
      executor.submit(new RunnableTask("task2"));
    } catch (RejectedExecutionException e) {
      log("요청 초과");
      // 포기, 다시 시도 등 다양한 고민을 하면 됨
      log(e);
    }
    executor.shutdown();

  }

  /**
   * 23:11:55.825 [     main] 요청 초과
   * 23:11:55.825 [pool-1-thread-1] task1 시작!
   * 23:11:55.828 [     main] java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.FutureTask@58372a00[Not completed, task = java.util.concurrent.Executors$RunnableAdapter@3b9a45b3[Wrapped task = thread.executor.RunnableTask@7699a589]] rejected from java.util.concurrent.ThreadPoolExecutor@4dd8dc3[Running, pool size = 1, active threads = 1, queued tasks = 0, completed tasks = 0]
   * 23:11:56.831 [pool-1-thread-1] task1 종료!
   */

}
