package thread.executor;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorBasicMain {

  public static void main(String[] args) {
    ExecutorService es = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS,
        new LinkedBlockingDeque<>());

    log("== 초기 상태 ==");
    printState(es);
    es.execute(new RunnableTask("taskA"));
    es.execute(new RunnableTask("taskB"));
    es.execute(new RunnableTask("taskC"));
    es.execute(new RunnableTask("taskD"));
    log("== 작업 수행 중 ==");
    printState(es);

    sleep(3000); // 3초 대기;
    log("== 작업 수행 후 ==");
    printState(es);

    es.shutdown(); // 스레드 풀 종료 (java21에서는 es.close())
    log("== shutdown 완료 ==");
    printState(es);
  }

  /**
   * 22:24:06.089 [     main] == 초기 상태 ==
   * 22:24:06.100 [     main] [pool = 0, active = 0, queuedTasks = 0, completedTasks = 0]
   * 22:24:06.102 [     main] == 작업 수행 중 ==
   * 22:24:06.102 [     main] [pool = 2, active = 2, queuedTasks = 2, completedTasks = 0]
   * 22:24:06.102 [pool-1-thread-1] taskA 시작!
   * 22:24:06.103 [pool-1-thread-2] taskB 시작!
   * 22:24:07.107 [pool-1-thread-1] taskA 종료!
   * 22:24:07.107 [pool-1-thread-2] taskB 종료!
   * 22:24:07.107 [pool-1-thread-1] taskC 시작!
   * 22:24:07.107 [pool-1-thread-2] taskD 시작!
   * 22:24:08.111 [pool-1-thread-1] taskC 종료!
   * 22:24:08.111 [pool-1-thread-2] taskD 종료!
   * 22:24:09.105 [     main] == 작업 수행 후 ==
   * 22:24:09.105 [     main] [pool = 2, active = 0, queuedTasks = 0, completedTasks = 4]
   * 22:24:09.105 [     main] == shutdown 완료 ==
   * 22:24:09.105 [     main] [pool = 0, active = 0, queuedTasks = 0, completedTasks = 4]
   */

}
