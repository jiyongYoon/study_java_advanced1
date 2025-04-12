package thread.executor.future;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableMainV1 {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ExecutorService es = Executors.newFixedThreadPool(1);// new ThreadPoolExecutor()를 호출해줌
    Future<Integer> future = es.submit(new MyCallable());// Callable을 사용하기 위해서는 submit()을 사용해야 한다.
    Integer result = future.get();
    log("result value = " + result);
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

  /** 스레드 생성이나 Runnable 없이 스레드를 사용해 작업을 처리하고 결과를 반환 받을 수 있다.
   * 21:55:55.650 [pool-1-thread-1] Callable 시작
   * 21:55:57.661 [pool-1-thread-1] create value = 4
   * 21:55:57.661 [pool-1-thread-1] Callable 완료
   * 21:55:57.661 [     main] result value =   <--- main 스레드는 처리된 값을 받아왔다. 새로운 스레드가 작업이 다 끝나길 기다린 것 같다.
   */
}
