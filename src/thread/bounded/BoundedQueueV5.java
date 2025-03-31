package thread.bounded;

import static util.MyLogger.log;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueueV5 implements BoundedQueue {

  // lock은 공유 (동일한 lock 사용)
  private final Lock lock = new ReentrantLock();

  // 생산자 스레드 대기 집합
  private final Condition producerCond = lock.newCondition();
  // 소비자 스레드 대기 집합
  private final Condition consumerCond = lock.newCondition();

  // 핵심 공유자원
  private final Queue<String> queue = new ArrayDeque<>();
  private final int max;

  public BoundedQueueV5(int max) {
    this.max = max;
  }

  @Override
  public void put(String data) {
    lock.lock();
    try {
      while (queue.size() == max) {
        log("[put] 큐가 가득 참, 생산자 대기");
        try {
          producerCond.await(); // 생산자 큐에서 대기
          log("[put] 생산자 깨어남");
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }

      queue.offer(data);
      log("[put] 생산자 데이터 저장, consumerCond.signal() 호출");
      consumerCond.signal(); // 소비자 깨움
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
          consumerCond.await(); // 소비자 큐에서 대기
          log("[take] 소비자 깨어남");
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      String data = queue.poll();
      log("[take] 소비자 데이터 획득, producerCond.signal() 호출");
      producerCond.signal(); // 생산자 깨움
      return data;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public String toString() {
    return queue.toString();
  }

  ///////////////////////////* BoundedQueueV5, 생산자, 소비자 대기공간 분리 *///////////////////////////
  /** 생산자 먼저
   * 22:17:27.545 [     main] == [생산자 먼저 실행] 시작, BoundedQueueV5==
   *
   * 22:17:27.548 [     main] 생산자 시작
   * 22:17:27.555 [producer1] [생산 시도] data1 -> []
   * 22:17:27.555 [producer1] [put] 생산자 데이터 저장, consumerCond.signal() 호출
   * 22:17:27.555 [producer1] [생산 완료] data1 -> [data1]
   * 22:17:27.655 [producer2] [생산 시도] data2 -> [data1]
   * 22:17:27.655 [producer2] [put] 생산자 데이터 저장, consumerCond.signal() 호출
   * 22:17:27.655 [producer2] [생산 완료] data2 -> [data1, data2]
   * 22:17:27.764 [producer3] [생산 시도] data3 -> [data1, data2]
   * 22:17:27.764 [producer3] [put] 큐가 가득 참, 생산자 대기
   *
   * 22:17:27.873 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 22:17:27.874 [     main] producer1: TERMINATED
   * 22:17:27.874 [     main] producer2: TERMINATED
   * 22:17:27.874 [     main] producer3: WAITING
   *
   * 22:17:27.874 [     main] 소비자 시작
   * 22:17:27.874 [consumer1] [소비 시도]     ? <- [data1, data2]
   * 22:17:27.875 [consumer1] [take] 소비자 데이터 획득, producerCond.signal() 호출
   * 22:17:27.875 [producer3] [put] 생산자 깨어남
   * 22:17:27.875 [consumer1] [소비 완료] data1 <- [data2]
   * 22:17:27.875 [producer3] [put] 생산자 데이터 저장, consumerCond.signal() 호출
   * 22:17:27.875 [producer3] [생산 완료] data3 -> [data2, data3]
   * 22:17:27.982 [consumer2] [소비 시도]     ? <- [data2, data3]
   * 22:17:27.982 [consumer2] [take] 소비자 데이터 획득, producerCond.signal() 호출
   * 22:17:27.982 [consumer2] [소비 완료] data2 <- [data3]
   * 22:17:28.093 [consumer3] [소비 시도]     ? <- [data3]
   * 22:17:28.093 [consumer3] [take] 소비자 데이터 획득, producerCond.signal() 호출
   * 22:17:28.093 [consumer3] [소비 완료] data3 <- []
   *
   * 22:17:28.202 [     main] 현재 상태 출력, 큐 데이터: []
   * 22:17:28.202 [     main] producer1: TERMINATED
   * 22:17:28.202 [     main] producer2: TERMINATED
   * 22:17:28.202 [     main] producer3: TERMINATED
   * 22:17:28.202 [     main] consumer1: TERMINATED
   * 22:17:28.202 [     main] consumer2: TERMINATED
   * 22:17:28.203 [     main] consumer3: TERMINATED
   * 22:17:28.203 [     main] == [생산자 먼저 실행] 종료, BoundedQueueV5==
   */

  /**
   * 22:18:27.442 [     main] == [소비자 먼저 실행] 시작, BoundedQueueV5==
   *
   * 22:18:27.445 [     main] 소비자 시작
   * 22:18:27.449 [consumer1] [소비 시도]     ? <- []
   * 22:18:27.449 [consumer1] [take] 큐에 데이터가 없음, 소비자 대기
   * 22:18:27.553 [consumer2] [소비 시도]     ? <- []
   * 22:18:27.553 [consumer2] [take] 큐에 데이터가 없음, 소비자 대기
   * 22:18:27.661 [consumer3] [소비 시도]     ? <- []
   * 22:18:27.661 [consumer3] [take] 큐에 데이터가 없음, 소비자 대기
   *
   * 22:18:27.768 [     main] 현재 상태 출력, 큐 데이터: []
   * 22:18:27.771 [     main] consumer1: WAITING
   * 22:18:27.771 [     main] consumer2: WAITING
   * 22:18:27.771 [     main] consumer3: WAITING
   *
   * 22:18:27.771 [     main] 생산자 시작
   * 22:18:27.772 [producer1] [생산 시도] data1 -> []
   * 22:18:27.772 [producer1] [put] 생산자 데이터 저장, consumerCond.signal() 호출
   * 22:18:27.773 [consumer1] [take] 소비자 깨어남
   * 22:18:27.773 [producer1] [생산 완료] data1 -> [data1]
   * 22:18:27.773 [consumer1] [take] 소비자 데이터 획득, producerCond.signal() 호출
   * 22:18:27.773 [consumer1] [소비 완료] data1 <- []
   * 22:18:27.877 [producer2] [생산 시도] data2 -> []
   * 22:18:27.877 [producer2] [put] 생산자 데이터 저장, consumerCond.signal() 호출
   * 22:18:27.877 [producer2] [생산 완료] data2 -> [data2]
   * 22:18:27.877 [consumer2] [take] 소비자 깨어남
   * 22:18:27.878 [consumer2] [take] 소비자 데이터 획득, producerCond.signal() 호출
   * 22:18:27.878 [consumer2] [소비 완료] data2 <- []
   * 22:18:27.985 [producer3] [생산 시도] data3 -> []
   * 22:18:27.986 [producer3] [put] 생산자 데이터 저장, consumerCond.signal() 호출
   * 22:18:27.986 [producer3] [생산 완료] data3 -> [data3]
   * 22:18:27.986 [consumer3] [take] 소비자 깨어남
   * 22:18:27.986 [consumer3] [take] 소비자 데이터 획득, producerCond.signal() 호출
   * 22:18:27.986 [consumer3] [소비 완료] data3 <- []
   *
   * 22:18:28.094 [     main] 현재 상태 출력, 큐 데이터: []
   * 22:18:28.094 [     main] consumer1: TERMINATED
   * 22:18:28.094 [     main] consumer2: TERMINATED
   * 22:18:28.094 [     main] consumer3: TERMINATED
   * 22:18:28.094 [     main] producer1: TERMINATED
   * 22:18:28.094 [     main] producer2: TERMINATED
   * 22:18:28.094 [     main] producer3: TERMINATED
   * 22:18:28.094 [     main] == [소비자 먼저 실행] 종료, BoundedQueueV5==
   */
}
