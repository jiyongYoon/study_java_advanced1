package thread.executor.reject;

import static util.MyLogger.log;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import thread.executor.RunnableTask;

public class RejectMainV4 {

  public static void main(String[] args) {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
        1,
        1,
        0,
        TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        new MyRejectedExecutionHandler()
    );

    executor.submit(new RunnableTask("task1"));
    executor.submit(new RunnableTask("task2"));
    executor.submit(new RunnableTask("task3"));
    executor.shutdown();
  }

  static class MyRejectedExecutionHandler implements RejectedExecutionHandler {

    static AtomicInteger count = new AtomicInteger(0);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
      int i = count.incrementAndGet();
      log("[경고] 거절된 누적 작업 수: " + i);
    }
  }

  /**
   * 23:16:09.688 [pool-1-thread-1] task1 시작!
   * 23:16:09.688 [     main] [경고] 거절된 누적 작업 수: 1
   * 23:16:09.691 [     main] [경고] 거절된 누적 작업 수: 2
   * 23:16:10.705 [pool-1-thread-1] task1 종료!
   */

}
