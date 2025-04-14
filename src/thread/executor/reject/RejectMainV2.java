package thread.executor.reject;

import static util.MyLogger.log;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import thread.executor.RunnableTask;

public class RejectMainV2 {

  public static void main(String[] args) {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
        1,
        1,
        0,
        TimeUnit.SECONDS,
        new SynchronousQueue<>(),
//        new AbortPolicy() // 기본 정책
        new ThreadPoolExecutor.DiscardPolicy() // 작업 버림
//        new ThreadPoolExecutor.CallerRunsPolicy() // 작업을 제출한 스레드가 대신 작업을 직접 수행
    );

    executor.submit(new RunnableTask("task1"));

    try {
      executor.submit(new RunnableTask("task2"));
      executor.submit(new RunnableTask("task3"));
    } catch (RejectedExecutionException e) {
      log("요청 초과");
      // 포기, 다시 시도 등 다양한 고민을 하면 됨
      log(e);
    }
    executor.shutdown();

  }

  /** 버린 로그가 안나옴 ㅋㅋㅋㅋ
   * 23:11:30.981 [pool-1-thread-1] task1 시작!
   * 23:11:31.993 [pool-1-thread-1] task1 종료!
   */

}
