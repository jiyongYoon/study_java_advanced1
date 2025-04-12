package thread.executor.future;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureCancelMain {

//  private static boolean mayInterruptIfRunning = true;
  private static boolean mayInterruptIfRunning = false;

  public static void main(String[] args) {
    ExecutorService es = Executors.newFixedThreadPool(1);
    Future<String> future = es.submit(new MyTask());

    // 일정 시간 후 취소 시도
    sleep(3000);

    // cancel() 호출
    log("future.cancel(" + mayInterruptIfRunning + ")");
    boolean cancelResult1 = future.cancel(mayInterruptIfRunning);
    log("cancel(" + mayInterruptIfRunning + ") result = " + cancelResult1);

    // 결과 확인
    try {
      String result = future.get(); // 블로킹 대기
      log("Future result = " + result);
    } catch (CancellationException e) {
      log("Future가 이미 취소되었습니다.");
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    } finally {
      es.shutdown(); // 스레드 풀 종료 (java21에서는 es.close())
    }
  }

  /** true
   * 22:46:11.905 [pool-1-thread-1] 작업 중 0
   * 22:46:12.910 [pool-1-thread-1] 작업 중 1
   * 22:46:13.911 [pool-1-thread-1] 작업 중 2
   * 22:46:14.896 [     main] future.cancel(true)
   * 22:46:14.897 [pool-1-thread-1] 인터럽트 발생! <--- 작업중인거 바로 중단 후 취소
   * 22:46:14.899 [     main] cancel(true) result = true
   * 22:46:14.899 [     main] Future가 이미 취소되었습니다.
   */

  /**
   * 22:46:37.746 [pool-1-thread-1] 작업 중 0
   * 22:46:38.750 [pool-1-thread-1] 작업 중 1
   * 22:46:39.750 [pool-1-thread-1] 작업 중 2
   * 22:46:40.735 [     main] future.cancel(false)
   * 22:46:40.737 [     main] cancel(false) result = true
   * 22:46:40.737 [     main] Future가 이미 취소되었습니다. <--- 작업중인건 그대로 두고 상태만 취소
   * 22:46:40.751 [pool-1-thread-1] 작업 중 3             <--- 작업은 모두 완료
   * 22:46:41.751 [pool-1-thread-1] 작업 중 4
   * 22:46:42.752 [pool-1-thread-1] 작업 중 5
   * 22:46:43.753 [pool-1-thread-1] 작업 중 6
   * 22:46:44.753 [pool-1-thread-1] 작업 중 7
   * 22:46:45.754 [pool-1-thread-1] 작업 중 8
   * 22:46:46.754 [pool-1-thread-1] 작업 중 9
   */

  static class MyTask implements Callable<String>  {

    @Override
    public String call() throws Exception {
      try {
        for (int i = 0; i < 10; i++) {
          log("작업 중 " + i);
          Thread.sleep(1000); // 1초 대기
        }
      } catch (InterruptedException e) {
        log("인터럽트 발생!");
        return "interrupted";
      } finally {
        return "completed";
      }
    }
  }

}
