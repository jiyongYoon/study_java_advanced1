package thread.bounded;

import static util.MyLogger.log;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueueV4 implements BoundedQueue {

  private final Lock lock = new ReentrantLock();

  // 스레드 대기 집합
  private final Condition condition = lock.newCondition();

  // 핵심 공유자원
  private final Queue<String> queue = new ArrayDeque<>();
  private final int max;

  public BoundedQueueV4(int max) {
    this.max = max;
  }

  @Override
  public void put(String data) {
    lock.lock();
    try {
      while (queue.size() == max) {
        log("[put] 큐가 가득 참, 생산자 대기");
        try {
          condition.await();
          log("[put] 생산자 깨어남");
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }

      queue.offer(data);
      log("[put] 생산자 데이터 저장, signal() 호출");
      condition.signal();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public String take() {
    lock.lock();
    try {
      while (queue.isEmpty()) {
        log("[take] 큐에 데이터가 없음, 소비자 대기");
        try {
          condition.await();
          log("[take] 소비자 깨어남");
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      String data = queue.poll();
      log("[take] 소비자 데이터 획득, signal() 호출");
      condition.signal();
      return data;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public String toString() {
    return queue.toString();
  }

  ///////////////////////////* BoundedQueueV4, condition.await(), condition.signal() *///////////////////////////
  /** 생산자 먼저
   * 22:16:38.533 [     main] == [생산자 먼저 실행] 시작, BoundedQueueV4==
   *
   * 22:16:38.536 [     main] 생산자 시작
   * 22:16:38.541 [producer1] [생산 시도] data1 -> []
   * 22:16:38.541 [producer1] [put] 생산자 데이터 저장, signal() 호출
   * 22:16:38.542 [producer1] [생산 완료] data1 -> [data1]
   * 22:16:38.648 [producer2] [생산 시도] data2 -> [data1]
   * 22:16:38.648 [producer2] [put] 생산자 데이터 저장, signal() 호출
   * 22:16:38.648 [producer2] [생산 완료] data2 -> [data1, data2]
   * 22:16:38.758 [producer3] [생산 시도] data3 -> [data1, data2]
   * 22:16:38.758 [producer3] [put] 큐가 가득 참, 생산자 대기
   *
   * 22:16:38.870 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 22:16:38.871 [     main] producer1: TERMINATED
   * 22:16:38.871 [     main] producer2: TERMINATED
   * 22:16:38.871 [     main] producer3: WAITING
   *
   * 22:16:38.871 [     main] 소비자 시작
   * 22:16:38.871 [consumer1] [소비 시도]     ? <- [data1, data2]
   * 22:16:38.871 [consumer1] [take] 소비자 데이터 획득, signal() 호출
   * 22:16:38.873 [producer3] [put] 생산자 깨어남
   * 22:16:38.873 [consumer1] [소비 완료] data1 <- [data2]
   * 22:16:38.873 [producer3] [put] 생산자 데이터 저장, signal() 호출
   * 22:16:38.873 [producer3] [생산 완료] data3 -> [data2, data3]
   * 22:16:38.979 [consumer2] [소비 시도]     ? <- [data2, data3]
   * 22:16:38.979 [consumer2] [take] 소비자 데이터 획득, signal() 호출
   * 22:16:38.979 [consumer2] [소비 완료] data2 <- [data3]
   * 22:16:39.090 [consumer3] [소비 시도]     ? <- [data3]
   * 22:16:39.090 [consumer3] [take] 소비자 데이터 획득, signal() 호출
   * 22:16:39.090 [consumer3] [소비 완료] data3 <- []
   *
   * 22:16:39.199 [     main] 현재 상태 출력, 큐 데이터: []
   * 22:16:39.199 [     main] producer1: TERMINATED
   * 22:16:39.199 [     main] producer2: TERMINATED
   * 22:16:39.199 [     main] producer3: TERMINATED
   * 22:16:39.199 [     main] consumer1: TERMINATED
   * 22:16:39.200 [     main] consumer2: TERMINATED
   * 22:16:39.200 [     main] consumer3: TERMINATED
   * 22:16:39.200 [     main] == [생산자 먼저 실행] 종료, BoundedQueueV4==
   */

  /** 소비자 먼저
   * 22:17:00.971 [     main] == [소비자 먼저 실행] 시작, BoundedQueueV4==
   *
   * 22:17:00.973 [     main] 소비자 시작
   * 22:17:00.977 [consumer1] [소비 시도]     ? <- []
   * 22:17:00.977 [consumer1] [take] 큐에 데이터가 없음, 소비자 대기
   * 22:17:01.091 [consumer2] [소비 시도]     ? <- []
   * 22:17:01.091 [consumer2] [take] 큐에 데이터가 없음, 소비자 대기
   * 22:17:01.203 [consumer3] [소비 시도]     ? <- []
   * 22:17:01.203 [consumer3] [take] 큐에 데이터가 없음, 소비자 대기
   *
   * 22:17:01.312 [     main] 현재 상태 출력, 큐 데이터: []
   * 22:17:01.315 [     main] consumer1: WAITING
   * 22:17:01.315 [     main] consumer2: WAITING
   * 22:17:01.315 [     main] consumer3: WAITING
   *
   * 22:17:01.315 [     main] 생산자 시작
   * 22:17:01.316 [producer1] [생산 시도] data1 -> []
   * 22:17:01.316 [producer1] [put] 생산자 데이터 저장, signal() 호출
   * 22:17:01.317 [consumer1] [take] 소비자 깨어남
   * 22:17:01.317 [consumer1] [take] 소비자 데이터 획득, signal() 호출
   * 22:17:01.317 [producer1] [생산 완료] data1 -> [data1]
   * 22:17:01.317 [consumer2] [take] 소비자 깨어남
   * 22:17:01.317 [consumer1] [소비 완료] data1 <- []
   * 22:17:01.317 [consumer2] [take] 큐에 데이터가 없음, 소비자 대기
   * 22:17:01.422 [producer2] [생산 시도] data2 -> []
   * 22:17:01.422 [producer2] [put] 생산자 데이터 저장, signal() 호출
   * 22:17:01.422 [producer2] [생산 완료] data2 -> [data2]
   * 22:17:01.422 [consumer3] [take] 소비자 깨어남
   * 22:17:01.422 [consumer3] [take] 소비자 데이터 획득, signal() 호출
   * 22:17:01.422 [consumer3] [소비 완료] data2 <- []
   * 22:17:01.422 [consumer2] [take] 소비자 깨어남
   * 22:17:01.422 [consumer2] [take] 큐에 데이터가 없음, 소비자 대기
   * 22:17:01.532 [producer3] [생산 시도] data3 -> []
   * 22:17:01.532 [producer3] [put] 생산자 데이터 저장, signal() 호출
   * 22:17:01.532 [consumer2] [take] 소비자 깨어남
   * 22:17:01.532 [producer3] [생산 완료] data3 -> [data3]
   * 22:17:01.533 [consumer2] [take] 소비자 데이터 획득, signal() 호출
   * 22:17:01.533 [consumer2] [소비 완료] data3 <- []
   *
   * 22:17:01.641 [     main] 현재 상태 출력, 큐 데이터: []
   * 22:17:01.641 [     main] consumer1: TERMINATED
   * 22:17:01.641 [     main] consumer2: TERMINATED
   * 22:17:01.641 [     main] consumer3: TERMINATED
   * 22:17:01.641 [     main] producer1: TERMINATED
   * 22:17:01.642 [     main] producer2: TERMINATED
   * 22:17:01.642 [     main] producer3: TERMINATED
   * 22:17:01.642 [     main] == [소비자 먼저 실행] 종료, BoundedQueueV4==
   */
}
