package thread.bounded;

import static util.MyLogger.log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BoundedQueueV6_4 implements BoundedQueue {

  // 핵심 공유자원
  private final BlockingQueue<String> queue;

  public BoundedQueueV6_4(int max) {
    this.queue = new ArrayBlockingQueue<>(max);
  }

  @Override
  public void put(String data) {
    queue.add(data); // java.lang.IllegalStateException: Queue full
  }

  @Override
  public String take() {
    return queue.remove(); // java.lang.NoSuchElementException: Queue empty
  }

  @Override
  public String toString() {
    return queue.toString();
  }

  /**
   * 23:11:06.811 [     main] == [생산자 먼저 실행] 시작, BoundedQueueV6_4==
   *
   * 23:11:06.814 [     main] 생산자 시작
   * 23:11:06.820 [producer1] [생산 시도] data1 -> []
   * 23:11:06.820 [producer1] [생산 완료] data1 -> [data1]
   * 23:11:06.933 [producer2] [생산 시도] data2 -> [data1]
   * 23:11:06.933 [producer2] [생산 완료] data2 -> [data1, data2]
   * 23:11:07.041 [producer3] [생산 시도] data3 -> [data1, data2]
   * Exception in thread "producer3" java.lang.IllegalStateException: Queue full
   * 	at java.base/java.util.AbstractQueue.add(AbstractQueue.java:98)
   * 	at java.base/java.util.concurrent.ArrayBlockingQueue.add(ArrayBlockingQueue.java:329)
   * 	at thread.bounded.BoundedQueueV6_4.put(BoundedQueueV6_4.java:20)
   * 	at thread.bounded.ProducerTask.run(ProducerTask.java:18)
   * 	at java.base/java.lang.Thread.run(Thread.java:840)
   *
   * 23:11:07.149 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 23:11:07.149 [     main] producer1: TERMINATED
   * 23:11:07.149 [     main] producer2: TERMINATED
   * 23:11:07.151 [     main] producer3: TERMINATED
   *
   * 23:11:07.151 [     main] 소비자 시작
   * 23:11:07.151 [consumer1] [소비 시도]     ? <- [data1, data2]
   * 23:11:07.151 [consumer1] [소비 완료] data1 <- [data2]
   * 23:11:07.259 [consumer2] [소비 시도]     ? <- [data2]
   * 23:11:07.259 [consumer2] [소비 완료] data2 <- []
   * 23:11:07.368 [consumer3] [소비 시도]     ? <- []
   * Exception in thread "consumer3" java.util.NoSuchElementException
   * 	at java.base/java.util.AbstractQueue.remove(AbstractQueue.java:117)
   * 	at thread.bounded.BoundedQueueV6_4.take(BoundedQueueV6_4.java:25)
   * 	at thread.bounded.ConsumerTask.run(ConsumerTask.java:16)
   * 	at java.base/java.lang.Thread.run(Thread.java:840)
   *
   * 23:11:07.477 [     main] 현재 상태 출력, 큐 데이터: []
   * 23:11:07.477 [     main] producer1: TERMINATED
   * 23:11:07.477 [     main] producer2: TERMINATED
   * 23:11:07.477 [     main] producer3: TERMINATED
   * 23:11:07.477 [     main] consumer1: TERMINATED
   * 23:11:07.477 [     main] consumer2: TERMINATED
   * 23:11:07.478 [     main] consumer3: TERMINATED
   * 23:11:07.478 [     main] == [생산자 먼저 실행] 종료, BoundedQueueV6_4==
   */

  /**
   * 23:11:23.756 [     main] == [소비자 먼저 실행] 시작, BoundedQueueV6_4==
   *
   * 23:11:23.758 [     main] 소비자 시작
   * 23:11:23.762 [consumer1] [소비 시도]     ? <- []
   * Exception in thread "consumer1" java.util.NoSuchElementException
   * 	at java.base/java.util.AbstractQueue.remove(AbstractQueue.java:117)
   * 	at thread.bounded.BoundedQueueV6_4.take(BoundedQueueV6_4.java:25)
   * 	at thread.bounded.ConsumerTask.run(ConsumerTask.java:16)
   * 	at java.base/java.lang.Thread.run(Thread.java:840)
   * 23:11:23.872 [consumer2] [소비 시도]     ? <- []
   * Exception in thread "consumer2" java.util.NoSuchElementException
   * 	at java.base/java.util.AbstractQueue.remove(AbstractQueue.java:117)
   * 	at thread.bounded.BoundedQueueV6_4.take(BoundedQueueV6_4.java:25)
   * 	at thread.bounded.ConsumerTask.run(ConsumerTask.java:16)
   * 	at java.base/java.lang.Thread.run(Thread.java:840)
   * 23:11:23.981 [consumer3] [소비 시도]     ? <- []
   * Exception in thread "consumer3" java.util.NoSuchElementException
   * 	at java.base/java.util.AbstractQueue.remove(AbstractQueue.java:117)
   * 	at thread.bounded.BoundedQueueV6_4.take(BoundedQueueV6_4.java:25)
   * 	at thread.bounded.ConsumerTask.run(ConsumerTask.java:16)
   * 	at java.base/java.lang.Thread.run(Thread.java:840)
   *
   * 23:11:24.088 [     main] 현재 상태 출력, 큐 데이터: []
   * 23:11:24.091 [     main] consumer1: TERMINATED
   * 23:11:24.091 [     main] consumer2: TERMINATED
   * 23:11:24.091 [     main] consumer3: TERMINATED
   *
   * 23:11:24.091 [     main] 생산자 시작
   * 23:11:24.092 [producer1] [생산 시도] data1 -> []
   * 23:11:24.092 [producer1] [생산 완료] data1 -> [data1]
   * 23:11:24.196 [producer2] [생산 시도] data2 -> [data1]
   * 23:11:24.196 [producer2] [생산 완료] data2 -> [data1, data2]
   * 23:11:24.305 [producer3] [생산 시도] data3 -> [data1, data2]
   * Exception in thread "producer3" java.lang.IllegalStateException: Queue full
   * 	at java.base/java.util.AbstractQueue.add(AbstractQueue.java:98)
   * 	at java.base/java.util.concurrent.ArrayBlockingQueue.add(ArrayBlockingQueue.java:329)
   * 	at thread.bounded.BoundedQueueV6_4.put(BoundedQueueV6_4.java:20)
   * 	at thread.bounded.ProducerTask.run(ProducerTask.java:18)
   * 	at java.base/java.lang.Thread.run(Thread.java:840)
   *
   * 23:11:24.414 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
   * 23:11:24.414 [     main] consumer1: TERMINATED
   * 23:11:24.414 [     main] consumer2: TERMINATED
   * 23:11:24.414 [     main] consumer3: TERMINATED
   * 23:11:24.414 [     main] producer1: TERMINATED
   * 23:11:24.414 [     main] producer2: TERMINATED
   * 23:11:24.414 [     main] producer3: TERMINATED
   * 23:11:24.415 [     main] == [소비자 먼저 실행] 종료, BoundedQueueV6_4==
   */
}
