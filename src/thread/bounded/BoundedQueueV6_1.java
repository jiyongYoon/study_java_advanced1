package thread.bounded;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BoundedQueueV6_1 implements BoundedQueue {

  // 핵심 공유자원
  private final BlockingQueue<String> queue;

  public BoundedQueueV6_1(int max) {
    this.queue = new ArrayBlockingQueue<>(max);
  }

  @Override
  public void put(String data) {
    try {
      queue.put(data);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String take() {
    try {
      return queue.take();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return queue.toString();
  }

  ///////////////////////////* BoundedQueueV5, 생산자, 소비자 대기공간 분리 *///////////////////////////
  /** 생산자 먼저
   * 22:52:28.338 [     main] == [생산자 먼저 실행] 시작, BoundedQueueV6_1==
   *
   * 22:52:28.340 [     main] 생산자 시작
   * 22:52:28.347 [producer1] [생산 시도] data1 -> []
   * 22:52:28.347 [producer1] [생산 완료] data1 -> [data1]
   * 22:52:28.452 [producer2] [생산 시도] data2 -> [data1]
   * 22:52:28.452 [producer2] [생산 완료] data2 -> [data1, data2]
   * 22:52:28.561 [producer3] [생산 시도] data3 -> [data1, data2]
   *
   * 22:52:28.670 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 22:52:28.671 [     main] producer1: TERMINATED
   * 22:52:28.671 [     main] producer2: TERMINATED
   * 22:52:28.671 [     main] producer3: WAITING
   *
   * 22:52:28.671 [     main] 소비자 시작
   * 22:52:28.673 [consumer1] [소비 시도]     ? <- [data1, data2]
   * 22:52:28.673 [producer3] [생산 완료] data3 -> [data2, data3]
   * 22:52:28.673 [consumer1] [소비 완료] data1 <- [data2, data3]
   * 22:52:28.780 [consumer2] [소비 시도]     ? <- [data2, data3]
   * 22:52:28.780 [consumer2] [소비 완료] data2 <- [data3]
   * 22:52:28.888 [consumer3] [소비 시도]     ? <- [data3]
   * 22:52:28.888 [consumer3] [소비 완료] data3 <- []
   *
   * 22:52:28.997 [     main] 현재 상태 출력, 큐 데이터: []
   * 22:52:28.997 [     main] producer1: TERMINATED
   * 22:52:28.997 [     main] producer2: TERMINATED
   * 22:52:28.997 [     main] producer3: TERMINATED
   * 22:52:28.997 [     main] consumer1: TERMINATED
   * 22:52:28.997 [     main] consumer2: TERMINATED
   * 22:52:28.998 [     main] consumer3: TERMINATED
   * 22:52:28.998 [     main] == [생산자 먼저 실행] 종료, BoundedQueueV6_1==
   */

  /** 소비자 먼저
   * 22:53:12.798 [     main] == [소비자 먼저 실행] 시작, BoundedQueueV6_1==
   *
   * 22:53:12.800 [     main] 소비자 시작
   * 22:53:12.803 [consumer1] [소비 시도]     ? <- []
   * 22:53:12.906 [consumer2] [소비 시도]     ? <- []
   * 22:53:13.015 [consumer3] [소비 시도]     ? <- []
   *
   * 22:53:13.124 [     main] 현재 상태 출력, 큐 데이터: []
   * 22:53:13.126 [     main] consumer1: WAITING
   * 22:53:13.127 [     main] consumer2: WAITING
   * 22:53:13.127 [     main] consumer3: WAITING
   *
   * 22:53:13.127 [     main] 생산자 시작
   * 22:53:13.128 [producer1] [생산 시도] data1 -> []
   * 22:53:13.128 [producer1] [생산 완료] data1 -> []
   * 22:53:13.128 [consumer1] [소비 완료] data1 <- []
   * 22:53:13.233 [producer2] [생산 시도] data2 -> []
   * 22:53:13.233 [producer2] [생산 완료] data2 -> [data2]
   * 22:53:13.233 [consumer2] [소비 완료] data2 <- []
   * 22:53:13.341 [producer3] [생산 시도] data3 -> []
   * 22:53:13.341 [consumer3] [소비 완료] data3 <- []
   * 22:53:13.341 [producer3] [생산 완료] data3 -> [data3]
   *
   * 22:53:13.450 [     main] 현재 상태 출력, 큐 데이터: []
   * 22:53:13.450 [     main] consumer1: TERMINATED
   * 22:53:13.450 [     main] consumer2: TERMINATED
   * 22:53:13.450 [     main] consumer3: TERMINATED
   * 22:53:13.450 [     main] producer1: TERMINATED
   * 22:53:13.450 [     main] producer2: TERMINATED
   * 22:53:13.450 [     main] producer3: TERMINATED
   * 22:53:13.451 [     main] == [소비자 먼저 실행] 종료, BoundedQueueV6_1==
   */
}
