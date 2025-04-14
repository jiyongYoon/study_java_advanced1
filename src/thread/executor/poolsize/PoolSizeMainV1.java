package thread.executor.poolsize;

import static thread.executor.ExecutorUtils.printState;
import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import thread.executor.RunnableTask;

public class PoolSizeMainV1 {

  public static void main(String[] args) {
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
    ExecutorService es =
        new ThreadPoolExecutor(2, 4, 3000, TimeUnit.MILLISECONDS, workQueue);
    printState(es);

    es.execute(new RunnableTask("task1"));
    printState(es, "task1");

    es.execute(new RunnableTask("task2"));
    printState(es, "task2");

    es.execute(new RunnableTask("task3"));
    printState(es, "task3");
    // 22:09:26.988 [     main] task3 -> [pool = 2, active = 2, queuedTasks = 1, completedTasks = 0]

    es.execute(new RunnableTask("task4"));
    printState(es, "task4");
    // 22:09:26.988 [     main] task4 -> [pool = 2, active = 2, queuedTasks = 2, completedTasks = 0]
    // 여기까지는 큐에 들어가니까 pool이 안늘어남

    // 이 아래부터는 큐에도 안들어가니까 pool 스레드가 늘어남
    es.execute(new RunnableTask("task5"));
    printState(es, "task5");
    // 22:09:26.988 [     main] task4 -> [pool = 2, active = 2, queuedTasks = 2, completedTasks = 0]

    es.execute(new RunnableTask("task6"));
    printState(es, "task6");
    // 22:09:26.988 [     main] task5 -> [pool = 3, active = 3, queuedTasks = 2, completedTasks = 0]

    try {
      // 큐도 꽉차고 threadPool도 다 늘어나서 작업하고 있어서 더이상 뭘 할수 없음 -> 예외 발생
      es.execute(new RunnableTask("task7"));
    } catch (RejectedExecutionException e) {
      log("task7 실행 거절 예외 발생: " + e);
      // 22:13:28.567 [     main] task7 실행 거절 예외 발생: java.util.concurrent.RejectedExecutionException: Task thread.executor.RunnableTask@6576fe71 rejected from java.util.concurrent.ThreadPoolExecutor@76fb509a[Running, pool size = 4, active threads = 4, queued tasks = 2, completed tasks = 0]
    }
    printState(es, "task7");
    // 22:13:28.567 [     main] task7 -> [pool = 4, active = 4, queuedTasks = 2, completedTasks = 0] -----> 1 ~ 6번 task까지만 들어감

    sleep(3000); // 모든 작업 종료될때까지 시간 기다림
    log("== 작업 수행 완료 ==");
    printState(es);
    // 22:13:31.571 [     main] [pool = 4, active = 0, queuedTasks = 0, completedTasks = 6]

    sleep(3000); // 큐가 다 차서 늘어난 스레드풀이 특정 시간이 지나면 다시 스레드가 사라짐
    log("== maximunPoolSize 대기 시간 초과 ==");
    printState(es);
    // 22:13:34.578 [     main] [pool = 2, active = 0, queuedTasks = 0, completedTasks = 6] // 풀에 있던 2개 스레드는 다시 사라지고 corePoolSize로 돌아옴

    es.shutdown();
    log("== shutdown 완료==");
    sleep(1000);
    printState(es);
    // 22:17:18.254 [     main] [pool = 0, active = 0, queuedTasks = 0, completedTasks = 6]
  }
}
