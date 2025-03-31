package thread.bounded;

import static util.MyLogger.log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BoundedQueueV6_3 implements BoundedQueue {

  // 핵심 공유자원
  private final BlockingQueue<String> queue;

  public BoundedQueueV6_3(int max) {
    this.queue = new ArrayBlockingQueue<>(max);
  }

  @Override
  public void put(String data) {
    boolean result = false;
    try {
      result = queue.offer(data, 1, TimeUnit.NANOSECONDS); // 성공하면 true, 실패하면 false 대기시간 후 반환
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    log("저장 시도 결과 = " + result);
  }

  @Override
  public String take() {
    try {
      return queue.poll(2, TimeUnit.SECONDS); // 실패시 null 대기시간 후 반환
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return queue.toString();
  }

  /**
   * 23:08:37.009 [     main] == [생산자 먼저 실행] 시작, BoundedQueueV6_3==
   *
   * 23:08:37.011 [     main] 생산자 시작
   * 23:08:37.018 [producer1] [생산 시도] data1 -> []
   * 23:08:37.018 [producer1] 저장 시도 결과 = true
   * 23:08:37.018 [producer1] [생산 완료] data1 -> [data1]
   * 23:08:37.127 [producer2] [생산 시도] data2 -> [data1]
   * 23:08:37.127 [producer2] 저장 시도 결과 = true
   * 23:08:37.127 [producer2] [생산 완료] data2 -> [data1, data2]
   * 23:08:37.237 [producer3] [생산 시도] data3 -> [data1, data2]
   * 23:08:37.237 [producer3] 저장 시도 결과 = false   ------------> 1 나노초 대기 후 큐에 자리 없어서 저장 실패
   * 23:08:37.237 [producer3] [생산 완료] data3 -> [data1, data2]
   *
   * 23:08:37.346 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 23:08:37.347 [     main] producer1: TERMINATED
   * 23:08:37.347 [     main] producer2: TERMINATED
   * 23:08:37.347 [     main] producer3: TERMINATED
   *
   * 23:08:37.347 [     main] 소비자 시작
   * 23:08:37.348 [consumer1] [소비 시도]     ? <- [data1, data2]
   * 23:08:37.349 [consumer1] [소비 완료] data1 <- [data2]
   * 23:08:37.457 [consumer2] [소비 시도]     ? <- [data2]
   * 23:08:37.457 [consumer2] [소비 완료] data2 <- []
   * 23:08:37.566 [consumer3] [소비 시도]     ? <- []  ------------> 데이터 없어서 소비 실패
   *
   * 23:08:37.676 [     main] 현재 상태 출력, 큐 데이터: []
   * 23:08:37.676 [     main] producer1: TERMINATED
   * 23:08:37.676 [     main] producer2: TERMINATED
   * 23:08:37.676 [     main] producer3: TERMINATED
   * 23:08:37.676 [     main] consumer1: TERMINATED
   * 23:08:37.676 [     main] consumer2: TERMINATED
   * 23:08:37.677 [     main] consumer3: TIMED_WAITING ------------> 대기 후
   * 23:08:37.677 [     main] == [생산자 먼저 실행] 종료, BoundedQueueV6_3==
   * 23:08:39.573 [consumer3] [소비 완료] null <- []    ------------> 2초 후에 null 리턴
   */

  /**
   * 23:05:58.590 [     main] == [소비자 먼저 실행] 시작, BoundedQueueV6_3==
   *
   * 23:05:58.592 [     main] 소비자 시작
   * 23:05:58.596 [consumer1] [소비 시도]     ? <- []
   * 23:05:58.599 [consumer1] [소비 완료] null <- []
   * 23:05:58.708 [consumer2] [소비 시도]     ? <- []
   * 23:05:58.708 [consumer2] [소비 완료] null <- []
   * 23:05:58.820 [consumer3] [소비 시도]     ? <- []
   * 23:05:58.820 [consumer3] [소비 완료] null <- []
   *
   * 23:05:58.929 [     main] 현재 상태 출력, 큐 데이터: []
   * 23:05:58.929 [     main] consumer1: TERMINATED
   * 23:05:58.929 [     main] consumer2: TERMINATED
   * 23:05:58.929 [     main] consumer3: TERMINATED
   *
   * 23:05:58.930 [     main] 생산자 시작
   * 23:05:58.931 [producer1] [생산 시도] data1 -> []
   * 23:05:58.931 [producer1] 저장 시도 결과 = true
   * 23:05:58.931 [producer1] [생산 완료] data1 -> [data1]
   * 23:05:59.039 [producer2] [생산 시도] data2 -> [data1]
   * 23:05:59.040 [producer2] 저장 시도 결과 = true
   * 23:05:59.040 [producer2] [생산 완료] data2 -> [data1, data2]
   * 23:05:59.149 [producer3] [생산 시도] data3 -> [data1, data2]
   * 23:05:59.149 [producer3] 저장 시도 결과 = false
   * 23:05:59.149 [producer3] [생산 완료] data3 -> [data1, data2]
   *
   * 23:05:59.260 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 23:05:59.260 [     main] consumer1: TERMINATED
   * 23:05:59.260 [     main] consumer2: TERMINATED
   * 23:05:59.260 [     main] consumer3: TERMINATED
   * 23:05:59.260 [     main] producer1: TERMINATED
   * 23:05:59.260 [     main] producer2: TERMINATED
   * 23:05:59.261 [     main] producer3: TERMINATED
   * 23:05:59.261 [     main] == [소비자 먼저 실행] 종료, BoundedQueueV6_3==
   */
}
