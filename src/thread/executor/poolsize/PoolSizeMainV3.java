package thread.executor.poolsize;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import thread.executor.RunnableTask;

public class PoolSizeMainV3 {

  public static void main(String[] args) {
    ExecutorService es = Executors.newCachedThreadPool();
    // 아래 코드와 동일한 결과다
    // ExecutorService es =
    // new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    log("pool 생성");
    printState(es);

    for (int i = 1; i <= 4; i++) {
      String taskName = "task" + i;
      es.execute(new RunnableTask(taskName));
      printState(es, taskName);
    }

    sleep(3000);
    log("== 작업 수행 완료 ==");
    printState(es);

    sleep(61000);
    log("== maximumPoolSize 대기 시간 초과 ==");
    printState(es);

    es.shutdown();
    log("== shutdown 완료 ==");
    printState(es);
  }

  /**
   * 22:43:06.335 [     main] pool 생성
   * 22:43:06.347 [     main] [pool = 0, active = 0, queuedTasks = 0, completedTasks = 0]
   * 22:43:06.350 [pool-1-thread-1] task1 시작!
   * 22:43:06.355 [     main] task1 -> [pool = 1, active = 1, queuedTasks = 0, completedTasks = 0]
   * 22:43:06.356 [     main] task2 -> [pool = 2, active = 2, queuedTasks = 0, completedTasks = 0]
   * 22:43:06.356 [pool-1-thread-2] task2 시작!
   * 22:43:06.356 [     main] task3 -> [pool = 3, active = 3, queuedTasks = 0, completedTasks = 0]
   * 22:43:06.356 [pool-1-thread-3] task3 시작!
   * 22:43:06.356 [     main] task4 -> [pool = 4, active = 4, queuedTasks = 0, completedTasks = 0]
   * 22:43:06.356 [pool-1-thread-4] task4 시작!
   * 22:43:07.355 [pool-1-thread-1] task1 종료!
   * 22:43:07.371 [pool-1-thread-2] task2 종료!
   * 22:43:07.371 [pool-1-thread-3] task3 종료!
   * 22:43:07.371 [pool-1-thread-4] task4 종료!
   * 22:43:09.360 [     main] == 작업 수행 완료 ==
   * 22:43:09.360 [     main] [pool = 4, active = 0, queuedTasks = 0, completedTasks = 4] <--- 긴급 스레드 살아있음
   * 22:44:10.361 [     main] == maximumPoolSize 대기 시간 초과 == <--- 60초 지남
   * 22:44:10.361 [     main] [pool = 0, active = 0, queuedTasks = 0, completedTasks = 4] <--- 풀에 모든 스레드 사라짐
   * 22:44:10.361 [     main] == shutdown 완료 ==
   * 22:44:10.361 [     main] [pool = 0, active = 0, queuedTasks = 0, completedTasks = 4]
   */

}
