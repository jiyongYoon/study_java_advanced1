package thread.executor.reject;

import static util.MyLogger.log;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import thread.executor.RunnableTask;

public class RejectMainV3 {

  public static void main(String[] args) {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
        1,
        1,
        0,
        TimeUnit.SECONDS,
        new SynchronousQueue<>(),
//        new AbortPolicy() // 기본 정책
//        new ThreadPoolExecutor.DiscardPolicy() // 작업 버림
        new ThreadPoolExecutor.CallerRunsPolicy() // 작업을 제출한 스레드가 대신 작업을 직접 수행
    );

    executor.submit(new RunnableTask("task1"));

    try {
      executor.submit(new RunnableTask("task2"));
      executor.submit(new RunnableTask("task3"));
      executor.submit(new RunnableTask("task4"));
      executor.submit(new RunnableTask("task5"));
    } catch (RejectedExecutionException e) {
      log("요청 초과");
      // 포기, 다시 시도 등 다양한 고민을 하면 됨
      log(e);
    }
    executor.shutdown();

  }

  /** main이 와서 작업중 ㅋㅋㅋ
   * 23:12:54.468 [     main] task2 시작!
   * 23:12:54.468 [pool-1-thread-1] task1 시작!
   * 23:12:55.484 [     main] task2 종료!
   * 23:12:55.484 [pool-1-thread-1] task1 종료!
   * 23:12:55.484 [     main] task3 시작!
   * 23:12:56.496 [     main] task3 종료!
   * 23:12:56.496 [     main] task5 시작!
   * 23:12:56.496 [pool-1-thread-1] task4 시작!
   * 23:12:57.509 [     main] task5 종료!
   * 23:12:57.509 [pool-1-thread-1] task4 종료!
   */

}
