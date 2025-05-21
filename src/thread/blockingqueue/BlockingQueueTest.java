package thread.blockingqueue;

import static util.MyLogger.log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingQueueTest {

  static class Producer implements Callable<Boolean> {

    private final Queue<String> queue;
    private final String name;
    private final Integer count;
    private final AtomicInteger offerCount = new AtomicInteger();

    public Producer(Queue<String> queue, String name, Integer count) {
      this.queue = queue;
      this.name = name;
      this.count = count;
    }

    @Override
    public Boolean call() throws Exception {
      for (int i = 0; i < count; i++) {
        String value = name + "--" + (i + 1);
        queue.offer(value);
        offerCount.incrementAndGet();
//        log(value);
      }
      return true;
    }

    public int getCount() {
      return offerCount.get();
    }
  }

  static class Consumer implements Callable<Boolean> {

    private final Queue<String> queue;
    private final Integer count;
    private final AtomicInteger pollCount = new AtomicInteger();

    public Consumer(Queue<String> queue, Integer count) {
      this.queue = queue;
      this.count = count;
    }

    @Override
    public Boolean call() throws Exception {
      for (int i = 0; i < count; i++) {
        String value = queue.poll();
        if (value != null) {
          pollCount.incrementAndGet();
//        log(value);
        }
      }
      return true;
    }

    public int getCount() {
      return pollCount.get();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    // 큐 선택: ArrayBlockingQueue (스레드 안전, 블로킹) 또는 LinkedList (스레드 안전하지 않음)
//    BlockingQueue<String> queue = new ArrayBlockingQueue<>(100000); // 큐 용량 증가
//     Queue<String> queue = new LinkedList<>(); // 이 라인을 주석 해제하여 비동기화 큐로 테스트
    ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    int dataPerProducer = 10_000; // 각 프로듀서가 생산할 데이터 수
    int producerCount = 10;      // 프로듀서 스레드 수
    int consumerCount = 10;      // 컨슈머 스레드 수
    int totalExpectedData = dataPerProducer * producerCount; // 총 예상 데이터 수

    // Consumer의 count는 모든 생산자가 생성하는 데이터의 총합 이상으로 설정하여
    // 모든 데이터를 소비할 수 있도록 보장해야 합니다.
    // LinkedList의 경우 큐가 비어있으면 poll()이 null을 반환하므로
    // 이 loop count만으로는 모든 데이터를 소비하지 못할 수 있습니다.
    // 실제 BlockingQueue 사용 시에는 take()를 사용하고, 작업 완료 신호를 주는 방식으로 처리하는 것이 일반적입니다.
    int consumerLoopCount = totalExpectedData + 10000; // 여유있게 설정

    List<Producer> producers = new ArrayList<>();
    for (int i = 0; i < producerCount; i++) {
      producers.add(new Producer(queue, "Producer-" + (i + 1), dataPerProducer));
    }

    List<Consumer> consumers = new ArrayList<>();
    for (int i = 0; i < producerCount; i++) { // 컨슈머 수도 프로듀서와 동일하게 설정
      consumers.add(new Consumer(queue, consumerLoopCount));
    }

    ExecutorService producerEs = Executors.newFixedThreadPool(producerCount);
    ExecutorService consumerEs = Executors.newFixedThreadPool(consumerCount);

    // 모든 프로듀서 작업 제출 및 완료 대기
    log("프로듀서 작업 시작...");
    List<Future<Boolean>> producerFutures = producerEs.invokeAll(producers);
//    producerEs.shutdown();
    // producerEs.awaitTermination(1, TimeUnit.MINUTES); // 프로듀서 완료까지 대기 (선택 사항)

    // 모든 컨슈머 작업 제출 및 완료 대기
    log("컨슈머 작업 시작...");
    List<Future<Boolean>> consumerFutures = consumerEs.invokeAll(consumers);
//    consumerEs.shutdown();
    // consumerEs.awaitTermination(1, TimeUnit.MINUTES); // 컨슈머 완료까지 대기 (선택 사항)


    // 모든 스레드가 종료될 때까지 대기
    // shutdown()만 호출하면 스레드 풀이 작업을 마치지 않고 종료될 수 있으므로 awaitTermination을 사용합니다.
    log("모든 스레드 종료 대기...");
    producerEs.awaitTermination(30, TimeUnit.SECONDS);
    consumerEs.awaitTermination(30, TimeUnit.SECONDS);


    // --- 결과 확인 ---
    long totalProduced = producers.stream()
        .mapToLong(Producer::getCount)
        .sum();

    long totalConsumed = consumers.stream()
        .mapToLong(Consumer::getCount)
        .sum();

    log("--- 최종 결과 ---");
    log("총 생산된 데이터 수: " + totalProduced);
    log("총 소비된 데이터 수: " + totalConsumed);
    log("큐에 남아있는 데이터 수: " + queue.size());

    if (totalProduced == totalConsumed && queue.isEmpty()) {
      log("🎉 모든 데이터가 정확하게 생산되고 소비되었습니다!");
    } else {
      log("🚨 경고: 데이터 불일치 발생! (생산: " + totalProduced + ", 소비: " + totalConsumed + ", 큐 잔여: " + queue.size() + ")");
      if (totalConsumed > totalProduced) {
        log("  (주의: 컨슈머가 null을 여러 번 poll 하여 '소비'로 카운트되었을 수 있습니다. pollCount 로직을 확인하세요.)");
      }
    }
    System.exit(0);
  }

  /* BlockingQueue
  22:58:45.405 [     main] 프로듀서 작업 시작...
  22:58:45.456 [     main] 컨슈머 작업 시작...
  22:58:45.477 [     main] 모든 스레드 종료 대기...
  22:59:45.497 [     main] --- 최종 결과 ---
  22:59:45.499 [     main] 총 생산된 데이터 수: 100000
  22:59:45.499 [     main] 총 소비된 데이터 수: 100000
  22:59:45.500 [     main] 큐에 남아있는 데이터 수: 0
  22:59:45.500 [     main] 🎉 모든 데이터가 정확하게 생산되고 소비되었습니다!
   */

  /* LinkedList -> 아주 난리구만
  23:00:27.346 [     main] 프로듀서 작업 시작...
  23:00:27.395 [     main] 컨슈머 작업 시작...
  23:00:27.475 [     main] 모든 스레드 종료 대기...
  23:01:27.491 [     main] --- 최종 결과 ---
  23:01:27.493 [     main] 총 생산된 데이터 수: 100000
  23:01:27.493 [     main] 총 소비된 데이터 수: 122
  23:01:27.493 [     main] 큐에 남아있는 데이터 수: 48472
  23:01:27.496 [     main] 🚨 경고: 데이터 불일치 발생! (생산: 100000, 소비: 122, 큐 잔여: 48472)
   */

  /* ConcurrentLinkedQueue
  23:08:53.041 [     main] 프로듀서 작업 시작...
  23:08:53.109 [     main] 컨슈머 작업 시작...
  23:08:53.199 [     main] 모든 스레드 종료 대기...
  23:09:53.218 [     main] --- 최종 결과 ---
  23:09:53.219 [     main] 총 생산된 데이터 수: 100000
  23:09:53.219 [     main] 총 소비된 데이터 수: 100000
  23:09:53.219 [     main] 큐에 남아있는 데이터 수: 0
  23:09:53.219 [     main] 🎉 모든 데이터가 정확하게 생산되고 소비되었습니다!
   */
}
