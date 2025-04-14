package thread.executor.poolsize;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import thread.executor.RunnableTask;

/**
 * 장점: CPU, 메모리 리소스 예측 가능!
 * 단점: 사용자 점진적 / 갑작스러운 요청 등에 대응을 못함
 *      -> 리소스 모니터링만 봐서도 예측이 어려울 수 있다. 큐에 엄청 쌓일 뿐....
 */
public class PoolSizeMainV2 {

  public static void main(String[] args) {
    ExecutorService es = Executors.newFixedThreadPool(2);
    // 아래 코드와 동일한 결과다
    // ExecutorService es =
    // new ThreadPoolExecutor(2, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());

    log("pool 생성");
    printState(es);

    for (int i = 1; i <= 6; i++) {
      String taskName = "task" + i;
      es.execute(new RunnableTask(taskName));
      printState(es, taskName);
    }
    es.shutdown();
    log("== shutdown 완료 ==");
  }

  /**
   * 22:31:25.073 [     main] pool 생성
   * 22:31:25.084 [     main] [pool = 0, active = 0, queuedTasks = 0, completedTasks = 0]
   * 22:31:25.087 [pool-1-thread-1] task1 시작!
   * 22:31:25.094 [     main] task1 -> [pool = 1, active = 1, queuedTasks = 0, completedTasks = 0]
   * 22:31:25.094 [     main] task2 -> [pool = 2, active = 2, queuedTasks = 0, completedTasks = 0]
   * 22:31:25.094 [pool-1-thread-2] task2 시작!
   * 22:31:25.094 [     main] task3 -> [pool = 2, active = 2, queuedTasks = 1, completedTasks = 0]     <----- 긴급 스레드를 만들지 않음
   * 22:31:25.095 [     main] task4 -> [pool = 2, active = 2, queuedTasks = 2, completedTasks = 0]
   * 22:31:25.095 [     main] task5 -> [pool = 2, active = 2, queuedTasks = 3, completedTasks = 0]
   * 22:31:25.095 [     main] task6 -> [pool = 2, active = 2, queuedTasks = 4, completedTasks = 0]
   * 22:31:25.095 [     main] == shutdown 완료 == -----> shutdown 호출되더라도 큐에 있던 작업들은 모두 완료
   * 22:31:26.095 [pool-1-thread-1] task1 종료!
   * 22:31:26.095 [pool-1-thread-1] task3 시작!
   * 22:31:26.097 [pool-1-thread-2] task2 종료!
   * 22:31:26.097 [pool-1-thread-2] task4 시작!
   * 22:31:27.097 [pool-1-thread-1] task3 종료!
   * 22:31:27.097 [pool-1-thread-1] task5 시작!
   * 22:31:27.106 [pool-1-thread-2] task4 종료!
   * 22:31:27.106 [pool-1-thread-2] task6 시작!
   * 22:31:28.112 [pool-1-thread-2] task6 종료!
   * 22:31:28.112 [pool-1-thread-1] task5 종료!
   */

}
