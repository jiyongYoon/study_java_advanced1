package thread.executor.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public class FutureDescription {

  public static void main(String[] args) {
    ExecutorService es = Executors.newFixedThreadPool(1);
    Future<Integer> future = es.submit(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        return 1;
      }
    });

    // 완료되지 않은 작업 취소 -> 리턴값: true - 성공 / false - 이미 완료되어 취소 실패
    future.cancel(true); // 작업 강제 중단하고 작업상태 취소로 바꿈
    future.cancel(false); // 실행중인 작업을 중단하지는 않고 작업상태만 취소로 바꿈
    // 취소 상태의 future.get() 호출 시 CancellationException 발생

    // 작업이 취소되었는지 여부 확인
    boolean isCancelled = future.isCancelled();

    // 작업이 완료되었는지 여부 확인
    boolean isDone = future.isDone();

    // Future의 상태 반환, java 19부터 지원
//    FutureState state = future.getState();

    // 작업 완료 결과 반환 (아직 완료되지 않은 경우 블로킹되고 대기)
    try {
      future.get();
    } catch (InterruptedException e) { // 대기 중에 현재 스레드가 인터럽트된 경우
      throw new RuntimeException(e);
    } catch (ExecutionException e) { // 계산 중에 예외가 발생한 경우
      throw new RuntimeException(e);
    }

    // 작업 완료 결과 반환 + 타임아웃 설정
    try {
      future.get(1, java.util.concurrent.TimeUnit.SECONDS); // 블로킹 대기 시간 설정
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    } catch (TimeoutException e) { // 주어진 시간 내에 작업 완료 안된 경우
    throw new RuntimeException(e);
    }
  }
}
