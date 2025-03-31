package thread.bounded;

import static util.MyLogger.log;

import java.util.ArrayDeque;
import java.util.Queue;

public class BoundedQueueV1 implements BoundedQueue {

  // 핵심 공유자원
  private final Queue<String> queue = new ArrayDeque<>();
  private final int max;

  public BoundedQueueV1(int max) {
    this.max = max;
  }

  @Override
  public synchronized void put(String data) {
    if (queue.size() == max) {
      log("[put] 큐가 가득 참, 버림: " + data);
      return;
    } else {
      queue.offer(data);
    }
  }

  @Override
  public synchronized String take() {
    if (queue.isEmpty()) {
      return null;
    }

    return queue.poll();
  }

  @Override
  public String toString() {
    return queue.toString();
  }

  ///////////////////////////* BoundedQueueV1 - 기다리지 않음 *///////////////////////////
  /** 생산자 먼저
   * 22:51:12.140 [     main] == [생산자 먼저 실행] 시작, BoundedQueueV1==
   *
   * 22:51:12.142 [     main] 생산자 시작
   * 22:51:12.149 [producer1] [생산 시도] data1 -> []
   * 22:51:12.149 [producer1] [생산 완료] data1 -> [data1]
   * 22:51:12.253 [producer2] [생산 시도] data2 -> [data1]
   * 22:51:12.253 [producer2] [생산 완료] data2 -> [data1, data2]
   * 22:51:12.363 [producer3] [생산 시도] data3 -> [data1, data2]
   * 22:51:12.364 [producer3] [put] 큐가 가득 참, 버림: data3
   * 22:51:12.364 [producer3] [생산 완료] data3 -> [data1, data2]
   *
   * 22:51:12.471 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 22:51:12.471 [     main] producer1: TERMINATED
   * 22:51:12.473 [     main] producer2: TERMINATED
   * 22:51:12.473 [     main] producer3: TERMINATED
   *
   * 22:51:12.473 [     main] 소비자 시작
   * 22:51:12.474 [consumer1] [소비 시도]     ? <- [data1, data2]
   * 22:51:12.474 [consumer1] [소비 완료] data1 <- [data2]
   * 22:51:12.580 [consumer2] [소비 시도]     ? <- [data2]
   * 22:51:12.580 [consumer2] [소비 완료] data2 <- []
   * 22:51:12.689 [consumer3] [소비 시도]     ? <- []
   * 22:51:12.689 [consumer3] [소비 완료] null <- []
   *
   * 22:51:12.797 [     main] 현재 상태 출력, 큐 데이터: []
   * 22:51:12.797 [     main] producer1: TERMINATED
   * 22:51:12.797 [     main] producer2: TERMINATED
   * 22:51:12.797 [     main] producer3: TERMINATED
   * 22:51:12.797 [     main] consumer1: TERMINATED
   * 22:51:12.797 [     main] consumer2: TERMINATED
   * 22:51:12.797 [     main] consumer3: TERMINATED
   * 22:51:12.798 [     main] == [생산자 먼저 실행] 종료, BoundedQueueV1==
   */

  /** 소비자 먼저
   * 22:52:59.269 [     main] == [소비자 먼저 실행] 시작, BoundedQueueV1==
   *
   * 22:52:59.271 [     main] 소비자 시작
   * 22:52:59.274 [consumer1] [소비 시도]     ? <- []
   * 22:52:59.277 [consumer1] [소비 완료] null <- []
   * 22:52:59.379 [consumer2] [소비 시도]     ? <- []
   * 22:52:59.379 [consumer2] [소비 완료] null <- []
   * 22:52:59.489 [consumer3] [소비 시도]     ? <- []
   * 22:52:59.489 [consumer3] [소비 완료] null <- []
   *
   * 22:52:59.600 [     main] 현재 상태 출력, 큐 데이터: []
   * 22:52:59.600 [     main] consumer1: TERMINATED
   * 22:52:59.600 [     main] consumer2: TERMINATED
   * 22:52:59.601 [     main] consumer3: TERMINATED
   *
   * 22:52:59.601 [     main] 생산자 시작
   * 22:52:59.602 [producer1] [생산 시도] data1 -> []
   * 22:52:59.602 [producer1] [생산 완료] data1 -> [data1]
   * 22:52:59.709 [producer2] [생산 시도] data2 -> [data1]
   * 22:52:59.709 [producer2] [생산 완료] data2 -> [data1, data2]
   * 22:52:59.818 [producer3] [생산 시도] data3 -> [data1, data2]
   * 22:52:59.818 [producer3] [put] 큐가 가득 참, 버림: data3
   * 22:52:59.818 [producer3] [생산 완료] data3 -> [data1, data2]
   *
   * 22:52:59.927 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 22:52:59.927 [     main] consumer1: TERMINATED
   * 22:52:59.927 [     main] consumer2: TERMINATED
   * 22:52:59.927 [     main] consumer3: TERMINATED
   * 22:52:59.927 [     main] producer1: TERMINATED
   * 22:52:59.927 [     main] producer2: TERMINATED
   * 22:52:59.927 [     main] producer3: TERMINATED
   * 22:52:59.928 [     main] == [소비자 먼저 실행] 종료, BoundedQueueV1==
   */
}
