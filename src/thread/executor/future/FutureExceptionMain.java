package thread.executor.future;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureExceptionMain {

  public static void main(String[] args) {
    ExecutorService es = Executors.newFixedThreadPool(1);
    log("작업 전달");
    Future<Integer> future = es.submit(new ExCallable());// 예외 발생
    sleep(1000);

    try {
      log("future.get() 호출 시도");
      Integer result = future.get();
      log("result = " + result);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      log("e = " + e);
      Throwable cause = e.getCause(); // 원본 예외
      log("cause = " + cause);
    } finally {
      es.shutdown(); // 스레드 풀 종료 (java21에서는 es.close())
    }
  }

  /**
   * 22:50:24.734 [     main] 작업 전달
   * 22:50:24.743 [pool-1-thread-1] Callable 실행, 예외 발생
   * 22:50:25.745 [     main] future.get() 호출 시도
   * 22:50:25.746 [     main] e = java.util.concurrent.ExecutionException: java.lang.IllegalArgumentException: 예외 발생
   * 22:50:25.747 [     main] cause = java.lang.IllegalArgumentException: 예외 발생
   */

  static class ExCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
      log("Callable 실행, 예외 발생");
      throw new IllegalArgumentException("예외 발생");
    }
  }

}
