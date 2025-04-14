package thread.executor.poolsize;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import thread.executor.RunnableTask;

public class PoolSizeMainV4 {

//  static final int TASK_SIZE = 1100; // 1. 일반
//  static final int TASK_SIZE = 1200; // 2. 긴급
  static final int TASK_SIZE = 1201; // 3. 거절


  public static void main(String[] args) {
    ExecutorService es =
        new ThreadPoolExecutor(
            100, 200, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));

    long startMs = System.currentTimeMillis();

    for (int i = 1; i <= TASK_SIZE; i++) {
      String taskName = "task" + i;
      try {
        es.execute(new RunnableTask(taskName));
        printState(es, taskName);
      } catch (RejectedExecutionException e) {
        log(taskName + " -> " + e);
      }
    }

//    es.close(); // 원래는 이렇게 모든 작업이 완료될 때까지 블로킹 대기해야하는데, java 19 이후부터 사용가능해서...

    es.shutdown();
    try {
      // 모든 작업이 완료될 때까지 대기 (예: 60초 타임아웃)
      if (!es.awaitTermination(15, TimeUnit.SECONDS)) {
        log("작업이 시간 내에 완료되지 않았습니다.");
      }
    } catch (InterruptedException e) {
      log("대기 중 인터럽트 발생: " + e);
    }
    long endMs = System.currentTimeMillis();
    log("time: " + (endMs - startMs) + "ms");
  }

  /** 1. 일반 ---> 초과 스레드가 안만들어지니까 작업 내내 100개 스레드만 동작
   * 22:59:58.796 [pool-1-thread-1] task1 시작!
   * 22:59:58.796 [     main] task1 -> [pool = 1, active = 1, queuedTasks = 0, completedTasks = 0]
   * 22:59:58.799 [     main] task2 -> [pool = 2, active = 2, queuedTasks = 0, completedTasks = 0]
   * 22:59:58.800 [pool-1-thread-2] task2 시작!
   * 22:59:58.800 [     main] task3 -> [pool = 3, active = 3, queuedTasks = 0, completedTasks = 0]
   * 22:59:58.800 [pool-1-thread-3] task3 시작!
   * .
   * .
   * .
   * 22:59:58.849 [     main] task1098 -> [pool = 100, active = 100, queuedTasks = 998, completedTasks = 0]
   * 22:59:58.849 [     main] task1099 -> [pool = 100, active = 100, queuedTasks = 999, completedTasks = 0]
   * 22:59:58.849 [     main] task1100 -> [pool = 100, active = 100, queuedTasks = 1000, completedTasks = 0]
   * 22:59:59.802 [pool-1-thread-5] task5 종료!
   * 22:59:59.802 [pool-1-thread-5] task101 시작!
   * 22:59:59.807 [pool-1-thread-23] task23 종료!
   * .
   * .
   * .
   * 23:00:09.918 [pool-1-thread-85] task1088 종료!
   * 23:00:09.918 [pool-1-thread-56] task1095 종료!
   * 23:00:09.918 [pool-1-thread-61] task1089 종료!
   * 23:00:09.920 [     main] time: 11136ms
   */

  /** 2. 긴급 ---> 200 개까지 초과스레드 100개 늘어나서 더 빨리 작업이 끝남
   * .
   * .
   * .
   * 23:01:09.820 [     main] task1198 -> [pool = 198, active = 198, queuedTasks = 1000, completedTasks = 0]
   * 23:01:09.821 [pool-1-thread-198] task1198 시작!
   * 23:01:09.821 [pool-1-thread-199] task1199 시작!
   * 23:01:09.821 [     main] task1199 -> [pool = 199, active = 199, queuedTasks = 1000, completedTasks = 0]
   * 23:01:09.821 [     main] task1200 -> [pool = 200, active = 200, queuedTasks = 1000, completedTasks = 0]
   * 23:01:09.821 [pool-1-thread-200] task1200 시작!
   * 23:01:10.748 [pool-1-thread-14] task14 종료!
   * 23:01:10.748 [pool-1-thread-14] task101 시작!
   * 23:01:10.754 [pool-1-thread-40] task40 종료!
   * .
   * .
   * .
   * 23:01:15.882 [pool-1-thread-130] task1080 종료!
   * 23:01:15.882 [pool-1-thread-195] task1099 종료!
   * 23:01:15.882 [pool-1-thread-196] task1098 종료!
   * 23:01:15.884 [     main] time: 6158ms
   */

  /** 3. 거절
   * .
   * .
   * .
   * 23:02:58.828 [     main] task1199 -> [pool = 199, active = 199, queuedTasks = 1000, completedTasks = 0]
   * 23:02:58.828 [     main] task1200 -> [pool = 200, active = 200, queuedTasks = 1000, completedTasks = 0]
   * 23:02:58.828 [pool-1-thread-200] task1200 시작!
   * 23:02:58.832 [     main] task1201 -> java.util.concurrent.RejectedExecutionException: Task thread.executor.RunnableTask@66cd51c3 rejected from java.util.concurrent.ThreadPoolExecutor@4dcbadb4[Running, pool size = 200, active threads = 200, queued tasks = 1000, completed tasks = 0]
   * 23:02:59.762 [pool-1-thread-8] task8 종료!
   * 23:02:59.762 [pool-1-thread-13] task13 종료!
   * 23:02:59.762 [pool-1-thread-8] task101 시작!
   * .
   * .
   * .
   * 23:03:04.892 [pool-1-thread-187] task1092 종료!
   * 23:03:04.892 [pool-1-thread-191] task1090 종료!
   * 23:03:04.892 [pool-1-thread-186] task1095 종료!
   * 23:03:04.893 [     main] time: 6156ms
   */
}
