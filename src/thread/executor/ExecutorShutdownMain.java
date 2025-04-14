package thread.executor;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorShutdownMain {

  public static void main(String[] args) {
    ExecutorService es = Executors.newFixedThreadPool(2);
    es.execute(new RunnableTask("taskA"));
    es.execute(new RunnableTask("taskB"));
    es.execute(new RunnableTask("taskC"));
    es.execute(new RunnableTask("longTask", 100_000)); // 100초 대기
    printState(es);
    log("== shutdown() 시작");
    shutdownAndAwaitTermination(es);
    log("== shutdown() 완료");
    printState(es);
  }

  /**
   21:58:32.350 [pool-1-thread-1] taskA 시작!
   21:58:32.350 [     main] [pool = 2, active = 2, queuedTasks = 2, completedTasks = 0]
   21:58:32.350 [pool-1-thread-2] taskB 시작!
   21:58:32.353 [     main] == shutdown() 시작
   21:58:33.365 [pool-1-thread-2] taskB 종료!
   21:58:33.365 [pool-1-thread-1] taskA 종료!
   21:58:33.365 [pool-1-thread-2] taskC 시작!
   21:58:33.365 [pool-1-thread-1] longTask 시작!
   21:58:34.367 [pool-1-thread-2] taskC 종료!
   21:58:42.362 [     main] 서비스 정상종료 실패 -> 강제 종료 시도        <----- shutdownNow() 호출되면 작업중인것들도 강제 종료 (인터럽트로)
   21:58:42.362 [pool-1-thread-1] 인터럽트 발생, sleep interrupted     <----- longTask 처리하던 스레드 인터럽트 발생
   21:58:42.363 [     main] == shutdown() 완료
   21:58:42.363 [     main] [pool = 0, active = 0, queuedTasks = 0, completedTasks = 4]
   Exception in thread "pool-1-thread-1" java.lang.RuntimeException: java.lang.InterruptedException: sleep interrupted
   at util.ThreadUtils.sleep(ThreadUtils.java:12)
   at thread.executor.RunnableTask.run(RunnableTask.java:23)
   at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
   at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
   at java.base/java.lang.Thread.run(Thread.java:840)
   Caused by: java.lang.InterruptedException: sleep interrupted
   at java.base/java.lang.Thread.sleep(Native Method)
   at util.ThreadUtils.sleep(ThreadUtils.java:9)
   ... 4 more
   */

  static void shutdownAndAwaitTermination(ExecutorService es) {
    es.shutdown(); // non-blocking, 큐에 작업까지 처리, 이후 풀의 스레드를 종료

    try {
      // 이미 대기중인 작업 모두 완료할 때까지 10초 기다림
      if (!es.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)) {
        log("서비스 정상종료 실패 -> 강제 종료 시도");
        es.shutdownNow();
        // 작업이 취소될 때까지 기다림
        if (!es.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)) {
          log("그래도 서비스가 종료되지 않습니다.");
        }
      }
    } catch (InterruptedException e) {
      // awaitTermination()으로 대기중인 현재 스레드가 인터럽트 될 수 있다.
      es.shutdownNow();
    }

  }


}
