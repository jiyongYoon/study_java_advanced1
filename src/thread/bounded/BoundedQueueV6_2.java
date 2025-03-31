package thread.bounded;

import static util.MyLogger.log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BoundedQueueV6_2 implements BoundedQueue {

  // 핵심 공유자원
  private final BlockingQueue<String> queue;

  public BoundedQueueV6_2(int max) {
    this.queue = new ArrayBlockingQueue<>(max);
  }

  @Override
  public void put(String data) {
    boolean result = queue.offer(data); // 성공하면 true 즉시 반환, 실패하면 false 즉시 반환
    log("저장 시도 결과 = " + result);
  }

  @Override
  public String take() {
    return queue.poll(); // 실패시 null 즉시 반환
  }

  @Override
  public String toString() {
    return queue.toString();
  }

  /** 생산자 먼저
   * 23:01:38.774 [     main] == [생산자 먼저 실행] 시작, BoundedQueueV6_2==
   *
   * 23:01:38.777 [     main] 생산자 시작
   * 23:01:38.783 [producer1] [생산 시도] data1 -> []
   * 23:01:38.783 [producer1] 저장 시도 결과 = true
   * 23:01:38.783 [producer1] [생산 완료] data1 -> [data1]
   * 23:01:38.889 [producer2] [생산 시도] data2 -> [data1]
   * 23:01:38.889 [producer2] 저장 시도 결과 = true
   * 23:01:38.889 [producer2] [생산 완료] data2 -> [data1, data2]
   * 23:01:38.998 [producer3] [생산 시도] data3 -> [data1, data2]
   * 23:01:38.998 [producer3] 저장 시도 결과 = false
   * 23:01:38.998 [producer3] [생산 완료] data3 -> [data1, data2]
   *
   * 23:01:39.107 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 23:01:39.108 [     main] producer1: TERMINATED
   * 23:01:39.108 [     main] producer2: TERMINATED
   * 23:01:39.108 [     main] producer3: TERMINATED
   *
   * 23:01:39.108 [     main] 소비자 시작
   * 23:01:39.109 [consumer1] [소비 시도]     ? <- [data1, data2]
   * 23:01:39.109 [consumer1] [소비 완료] data1 <- [data2]
   * 23:01:39.216 [consumer2] [소비 시도]     ? <- [data2]
   * 23:01:39.216 [consumer2] [소비 완료] data2 <- []
   * 23:01:39.326 [consumer3] [소비 시도]     ? <- []
   * 23:01:39.326 [consumer3] [소비 완료] null <- []
   *
   * 23:01:39.434 [     main] 현재 상태 출력, 큐 데이터: []
   * 23:01:39.434 [     main] producer1: TERMINATED
   * 23:01:39.434 [     main] producer2: TERMINATED
   * 23:01:39.434 [     main] producer3: TERMINATED
   * 23:01:39.434 [     main] consumer1: TERMINATED
   * 23:01:39.434 [     main] consumer2: TERMINATED
   * 23:01:39.434 [     main] consumer3: TERMINATED
   * 23:01:39.435 [     main] == [생산자 먼저 실행] 종료, BoundedQueueV6_2==
   */

  /** 소비자 먼저
   * 23:02:06.242 [     main] == [소비자 먼저 실행] 시작, BoundedQueueV6_2==
   *
   * 23:02:06.244 [     main] 소비자 시작
   * 23:02:06.247 [consumer1] [소비 시도]     ? <- []
   * 23:02:06.251 [consumer1] [소비 완료] null <- []
   * 23:02:06.355 [consumer2] [소비 시도]     ? <- []
   * 23:02:06.355 [consumer2] [소비 완료] null <- []
   * 23:02:06.462 [consumer3] [소비 시도]     ? <- []
   * 23:02:06.462 [consumer3] [소비 완료] null <- []
   *
   * 23:02:06.574 [     main] 현재 상태 출력, 큐 데이터: []
   * 23:02:06.574 [     main] consumer1: TERMINATED
   * 23:02:06.574 [     main] consumer2: TERMINATED
   * 23:02:06.574 [     main] consumer3: TERMINATED
   *
   * 23:02:06.574 [     main] 생산자 시작
   * 23:02:06.575 [producer1] [생산 시도] data1 -> []
   * 23:02:06.575 [producer1] 저장 시도 결과 = true
   * 23:02:06.576 [producer1] [생산 완료] data1 -> [data1]
   * 23:02:06.684 [producer2] [생산 시도] data2 -> [data1]
   * 23:02:06.684 [producer2] 저장 시도 결과 = true
   * 23:02:06.684 [producer2] [생산 완료] data2 -> [data1, data2]
   * 23:02:06.793 [producer3] [생산 시도] data3 -> [data1, data2]
   * 23:02:06.793 [producer3] 저장 시도 결과 = false
   * 23:02:06.793 [producer3] [생산 완료] data3 -> [data1, data2]
   *
   * 23:02:06.901 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 23:02:06.901 [     main] consumer1: TERMINATED
   * 23:02:06.901 [     main] consumer2: TERMINATED
   * 23:02:06.901 [     main] consumer3: TERMINATED
   * 23:02:06.901 [     main] producer1: TERMINATED
   * 23:02:06.901 [     main] producer2: TERMINATED
   * 23:02:06.901 [     main] producer3: TERMINATED
   * 23:02:06.902 [     main] == [소비자 먼저 실행] 종료, BoundedQueueV6_2==
   */
}
