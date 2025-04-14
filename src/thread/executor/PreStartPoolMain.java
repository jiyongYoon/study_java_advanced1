package thread.executor;

import static thread.executor.ExecutorUtils.printState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class PreStartPoolMain {

  public static void main(String[] args) {
    ExecutorService es = Executors.newFixedThreadPool(1000);
    printState(es);
    ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) es;
    poolExecutor.prestartAllCoreThreads();
    printState(es);
    es.shutdown();
  }
  /** 작업을 넣지 않아도 스레드를 풀에 미리 만들어 둘 수 있다.
   * 22:26:42.344 [     main] [pool = 0, active = 0, queuedTasks = 0, completedTasks = 0]
   * 22:26:42.414 [     main] [pool = 1000, active = 0, queuedTasks = 0, completedTasks = 0]
   */

}
