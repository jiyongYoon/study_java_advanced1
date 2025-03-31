package thread.bounded;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.ArrayDeque;
import java.util.Queue;

public class BoundedQueueV2 implements BoundedQueue {

  // 핵심 공유자원
  private final Queue<String> queue = new ArrayDeque<>();
  private final int max;

  public BoundedQueueV2(int max) {
    this.max = max;
  }

  @Override
  public synchronized void put(String data) {
    while (queue.size() == max) {
      log("[put] 큐가 가득 참, 생산자 대기");
      sleep(1000);
    }
    queue.offer(data);
  }

  @Override
  public synchronized String take() {
    while (queue.isEmpty()) {
      log("[take] 큐에 데이터가 없음, 소비자 대기");
      sleep(1000);
    }

    return queue.poll();
  }

  @Override
  public String toString() {
    return queue.toString();
  }

  ///////////////////////////* BoundedQueueV2, while문으로 대기 *///////////////////////////
  /** 생산자 먼저
   * 23:09:51.660 [     main] == [생산자 먼저 실행] 시작, BoundedQueueV2==
   *
   * 23:09:51.662 [     main] 생산자 시작
   * 23:09:51.669 [producer1] [생산 시도] data1 -> []
   * 23:09:51.669 [producer1] [생산 완료] data1 -> [data1]
   * 23:09:51.772 [producer2] [생산 시도] data2 -> [data1]
   * 23:09:51.772 [producer2] [생산 완료] data2 -> [data1, data2]
   * 23:09:51.881 [producer3] [생산 시도] data3 -> [data1, data2]
   * 23:09:51.881 [producer3] [put] 큐가 가득 참, 생산자 대기
   *
   * 23:09:51.991 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 23:09:51.992 [     main] producer1: TERMINATED
   * 23:09:51.992 [     main] producer2: TERMINATED
   * 23:09:51.992 [     main] producer3: TIMED_WAITING   <--- 생산자 먼저 시 얘가 락 가지고 무한루프중
   *
   * 23:09:51.992 [     main] 소비자 시작
   * 23:09:51.993 [consumer1] [소비 시도]     ? <- [data1, data2]
   * 23:09:52.097 [consumer2] [소비 시도]     ? <- [data1, data2]
   * 23:09:52.199 [consumer3] [소비 시도]     ? <- [data1, data2]
   *
   * 23:09:52.300 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 23:09:52.300 [     main] producer1: TERMINATED
   * 23:09:52.300 [     main] producer2: TERMINATED
   * 23:09:52.300 [     main] producer3: TIMED_WAITING
   * 23:09:52.300 [     main] consumer1: BLOCKED
   * 23:09:52.300 [     main] consumer2: BLOCKED
   * 23:09:52.300 [     main] consumer3: BLOCKED
   * 23:09:52.300 [     main] == [생산자 먼저 실행] 종료, BoundedQueueV2==
   * 23:09:52.893 [producer3] [put] 큐가 가득 참, 생산자 대기
   * 23:09:53.908 [producer3] [put] 큐가 가득 참, 생산자 대기
   * 23:09:54.921 [producer3] [put] 큐가 가득 참, 생산자 대기
   * 23:09:55.935 [producer3] [put] 큐가 가득 참, 생산자 대기
   * 23:09:56.946 [producer3] [put] 큐가 가득 참, 생산자 대기
   * 23:09:57.958 [producer3] [put] 큐가 가득 참, 생산자 대기
   * ... (무한 반복) ...
   */

  /** 소비자 먼저
   * 23:15:11.476 [     main] == [소비자 먼저 실행] 시작, BoundedQueueV2==
   *
   * 23:15:11.478 [     main] 소비자 시작
   * 23:15:11.481 [consumer1] [소비 시도]     ? <- []
   * 23:15:11.482 [consumer1] [take] 큐에 데이터가 없음, 소비자 대기
   * 23:15:11.594 [consumer2] [소비 시도]     ? <- []
   * 23:15:11.702 [consumer3] [소비 시도]     ? <- []
   *
   * 23:15:11.802 [     main] 현재 상태 출력, 큐 데이터: []
   * 23:15:11.804 [     main] consumer1: TIMED_WAITING   <--- 소비자 먼저 시 얘가 락 가지고 무한루프중
   * 23:15:11.805 [     main] consumer2: BLOCKED
   * 23:15:11.805 [     main] consumer3: BLOCKED
   *
   * 23:15:11.805 [     main] 생산자 시작
   * 23:15:11.806 [producer1] [생산 시도] data1 -> []
   * 23:15:11.907 [producer2] [생산 시도] data2 -> []
   * 23:15:12.008 [producer3] [생산 시도] data3 -> []
   *
   * 23:15:12.108 [     main] 현재 상태 출력, 큐 데이터: []
   * 23:15:12.108 [     main] consumer1: TIMED_WAITING
   * 23:15:12.108 [     main] consumer2: BLOCKED
   * 23:15:12.108 [     main] consumer3: BLOCKED
   * 23:15:12.108 [     main] producer1: BLOCKED
   * 23:15:12.108 [     main] producer2: BLOCKED
   * 23:15:12.108 [     main] producer3: BLOCKED
   * 23:15:12.108 [     main] == [소비자 먼저 실행] 종료, BoundedQueueV2==
   * 23:15:12.485 [consumer1] [take] 큐에 데이터가 없음, 소비자 대기
   * 23:15:13.498 [consumer1] [take] 큐에 데이터가 없음, 소비자 대기
   * 23:15:14.500 [consumer1] [take] 큐에 데이터가 없음, 소비자 대기
   * 23:15:15.510 [consumer1] [take] 큐에 데이터가 없음, 소비자 대기
   */
}
