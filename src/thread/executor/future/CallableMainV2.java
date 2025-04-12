package thread.executor.future;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableMainV2 {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ExecutorService es = Executors.newFixedThreadPool(1);
    log("submit() 호출");
    Future<Integer> future = es.submit(new MyCallable());
    log("future 즉시 반환, future = " + future);

//    sleep(3000); // 만약 작업 후에 get을 요청하면? -------------> 2번 출력

    log("future.get() [블로킹] 메서드 호출 시작 -> main 스레드 WAITING");
    Integer result = future.get();
    log("future.get() [블로킹] 메서드 호출 완료 -> main 스레드 RUNNABLE");

    log("result value = " + result);
    log("future 완료, future = " + future);
    es.shutdown(); // 스레드 풀 종료 (java21에서는 es.close())
  }


  static class MyCallable implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
      log("Callable 시작");
      sleep(2000);
      int value = new Random().nextInt(10);
      log("create value = " + value);
      log("Callable 완료");
      return value;
    }
  }

  /** 1번
   * 22:03:03.590 [     main] submit() 호출
   * 22:03:03.601 [pool-1-thread-1] Callable 시작
   * 22:03:03.603 [     main] future 즉시 반환, future = java.util.concurrent.FutureTask@7cc355be[Not completed, task = thread.executor.future.CallableMainV2$MyCallable@34a245ab] <--- 작업 완료 여부와 작업(Task)내용
   * 22:03:03.603 [     main] future.get() [블로킹] 메서드 호출 시작 -> main 스레드 WAITING
   * 22:03:05.606 [pool-1-thread-1] create value = 0
   * 22:03:05.607 [pool-1-thread-1] Callable 완료
   * 22:03:05.607 [     main] future.get() [블로킹] 메서드 호출 완료 -> main 스레드 RUNNABLE
   * 22:03:05.607 [     main] result value = 0
   * 22:03:05.607 [     main] future 완료, future = java.util.concurrent.FutureTask@7cc355be[Completed normally]
   */

  /** 2번
   * 22:11:38.509 [     main] submit() 호출
   * 22:11:38.519 [pool-1-thread-1] Callable 시작
   * 22:11:38.520 [     main] future 즉시 반환, future = java.util.concurrent.FutureTask@7cc355be[Not completed, task = thread.executor.future.CallableMainV2$MyCallable@34a245ab]
   * 22:11:40.523 [pool-1-thread-1] create value = 1
   * 22:11:40.523 [pool-1-thread-1] Callable 완료
   * 22:11:41.522 [     main] future.get() [블로킹] 메서드 호출 시작 -> main 스레드 WAITING     <--------- future.get()을 했는데 이미 task가 동작을 다 했기 때문에 블로킹 없이 바로 다음코드로 넘어간다.
   * 22:11:41.522 [     main] future.get() [블로킹] 메서드 호출 완료 -> main 스레드 RUNNABLE
   * 22:11:41.522 [     main] result value = 1
   * 22:11:41.522 [     main] future 완료, future = java.util.concurrent.FutureTask@7cc355be[Completed normally]
   */
}
