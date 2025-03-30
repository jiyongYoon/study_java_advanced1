package thread.bounded;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.ArrayList;
import java.util.List;

public class BoundedMain {

  public static void main(String[] args) {
    // 1. BoundedQueue 선택
//    BoundedQueue queue = new BoundedQueueV1(2);
//    BoundedQueue queue = new BoundedQueueV2(2);
    BoundedQueue queue = new BoundedQueueV3(2);

    // 2. 생산자, 소비자 실행 순서 선택, 반드시 하나만 선택!
//    producerFirst(queue);
    consumerFirst(queue);

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

    ///////////////////////////* BoundedQueueV3, wait(), notify() *///////////////////////////
    /** 생산자 먼저
     * 23:31:06.187 [     main] == [생산자 먼저 실행] 시작, BoundedQueueV3==
     *
     * 23:31:06.189 [     main] 생산자 시작
     * 23:31:06.196 [producer1] [생산 시도] data1 -> []
     * 23:31:06.196 [producer1] [put] 생산자 데이터 저장, notify() 호출
     * 23:31:06.196 [producer1] [생산 완료] data1 -> [data1]
     * 23:31:06.302 [producer2] [생산 시도] data2 -> [data1]
     * 23:31:06.302 [producer2] [put] 생산자 데이터 저장, notify() 호출
     * 23:31:06.302 [producer2] [생산 완료] data2 -> [data1, data2]
     * 23:31:06.413 [producer3] [생산 시도] data3 -> [data1, data2]
     * 23:31:06.413 [producer3] [put] 큐가 가득 참, 생산자 대기
     *
     * 23:31:06.523 [     main] 현재 상태 출력, 큐 데이터: [data1, data2]
     * 23:31:06.524 [     main] producer1: TERMINATED
     * 23:31:06.524 [     main] producer2: TERMINATED
     * 23:31:06.524 [     main] producer3: WAITING
     *
     * 23:31:06.524 [     main] 소비자 시작
     * 23:31:06.525 [consumer1] [소비 시도]     ? <- [data1, data2]
     * 23:31:06.525 [consumer1] [take] 소비자 데이터 획득, notify() 호출
     * 23:31:06.525 [producer3] [put] 생산자 깨어남
     * 23:31:06.526 [consumer1] [소비 완료] data1 <- [data2]
     * 23:31:06.526 [producer3] [put] 생산자 데이터 저장, notify() 호출
     * 23:31:06.526 [producer3] [생산 완료] data3 -> [data2, data3]
     * 23:31:06.631 [consumer2] [소비 시도]     ? <- [data2, data3]
     * 23:31:06.631 [consumer2] [take] 소비자 데이터 획득, notify() 호출
     * 23:31:06.631 [consumer2] [소비 완료] data2 <- [data3]
     * 23:31:06.741 [consumer3] [소비 시도]     ? <- [data3]
     * 23:31:06.741 [consumer3] [take] 소비자 데이터 획득, notify() 호출
     * 23:31:06.741 [consumer3] [소비 완료] data3 <- []
     *
     * 23:31:06.851 [     main] 현재 상태 출력, 큐 데이터: []
     * 23:31:06.851 [     main] producer1: TERMINATED
     * 23:31:06.851 [     main] producer2: TERMINATED
     * 23:31:06.851 [     main] producer3: TERMINATED
     * 23:31:06.851 [     main] consumer1: TERMINATED
     * 23:31:06.851 [     main] consumer2: TERMINATED
     * 23:31:06.851 [     main] consumer3: TERMINATED
     * 23:31:06.852 [     main] == [생산자 먼저 실행] 종료, BoundedQueueV3==
     */

    /** 소비자 먼저
     * 23:31:56.447 [     main] == [소비자 먼저 실행] 시작, BoundedQueueV3==
     *
     * 23:31:56.450 [     main] 소비자 시작
     * 23:31:56.453 [consumer1] [소비 시도]     ? <- []
     * 23:31:56.453 [consumer1] [take] 큐에 데이터가 없음, 소비자 대기
     * 23:31:56.561 [consumer2] [소비 시도]     ? <- []
     * 23:31:56.561 [consumer2] [take] 큐에 데이터가 없음, 소비자 대기
     * 23:31:56.669 [consumer3] [소비 시도]     ? <- []
     * 23:31:56.669 [consumer3] [take] 큐에 데이터가 없음, 소비자 대기
     *
     * 23:31:56.779 [     main] 현재 상태 출력, 큐 데이터: []
     * 23:31:56.782 [     main] consumer1: WAITING
     * 23:31:56.783 [     main] consumer2: WAITING
     * 23:31:56.783 [     main] consumer3: WAITING
     *
     * 23:31:56.783 [     main] 생산자 시작
     * 23:31:56.783 [producer1] [생산 시도] data1 -> []
     * 23:31:56.784 [producer1] [put] 생산자 데이터 저장, notify() 호출
     * 23:31:56.784 [consumer1] [take] 소비자 깨어남
     * 23:31:56.784 [producer1] [생산 완료] data1 -> [data1]
     * 23:31:56.784 [consumer1] [take] 소비자 데이터 획득, notify() 호출
     * 23:31:56.784 [consumer2] [take] 소비자 깨어남
     * 23:31:56.784 [consumer2] [take] 큐에 데이터가 없음, 소비자 대기
     * 23:31:56.784 [consumer1] [소비 완료] data1 <- []
     * 23:31:56.890 [producer2] [생산 시도] data2 -> []
     * 23:31:56.890 [producer2] [put] 생산자 데이터 저장, notify() 호출
     * 23:31:56.891 [producer2] [생산 완료] data2 -> [data2]
     * 23:31:56.891 [consumer3] [take] 소비자 깨어남
     * 23:31:56.891 [consumer3] [take] 소비자 데이터 획득, notify() 호출
     * 23:31:56.891 [consumer3] [소비 완료] data2 <- []
     * 23:31:56.891 [consumer2] [take] 소비자 깨어남
     * 23:31:56.891 [consumer2] [take] 큐에 데이터가 없음, 소비자 대기
     * 23:31:57.001 [producer3] [생산 시도] data3 -> []
     * 23:31:57.001 [producer3] [put] 생산자 데이터 저장, notify() 호출
     * 23:31:57.001 [producer3] [생산 완료] data3 -> [data3]
     * 23:31:57.001 [consumer2] [take] 소비자 깨어남
     * 23:31:57.002 [consumer2] [take] 소비자 데이터 획득, notify() 호출
     * 23:31:57.002 [consumer2] [소비 완료] data3 <- []
     *
     * 23:31:57.110 [     main] 현재 상태 출력, 큐 데이터: []
     * 23:31:57.110 [     main] consumer1: TERMINATED
     * 23:31:57.110 [     main] consumer2: TERMINATED
     * 23:31:57.110 [     main] consumer3: TERMINATED
     * 23:31:57.110 [     main] producer1: TERMINATED
     * 23:31:57.110 [     main] producer2: TERMINATED
     * 23:31:57.111 [     main] producer3: TERMINATED
     * 23:31:57.111 [     main] == [소비자 먼저 실행] 종료, BoundedQueueV3==
     */
  }

  private static void producerFirst(BoundedQueue queue) {
    log("== [생산자 먼저 실행] 시작, " + queue.getClass().getSimpleName() + "==");

    List<Thread> threads = new ArrayList<>();

    startProducer(queue, threads);
    printAllState(queue, threads);
    startConsumer(queue, threads);
    printAllState(queue, threads);

    log("== [생산자 먼저 실행] 종료, " + queue.getClass().getSimpleName() + "==");
  }

  private static void consumerFirst(BoundedQueue queue) {
    log("== [소비자 먼저 실행] 시작, " + queue.getClass().getSimpleName() + "==");

    List<Thread> threads = new ArrayList<>();

    startConsumer(queue, threads);
    printAllState(queue, threads);
    startProducer(queue, threads);
    printAllState(queue, threads);

    log("== [소비자 먼저 실행] 종료, " + queue.getClass().getSimpleName() + "==");
  }

  private static void startProducer(BoundedQueue queue, List<Thread> threads) {
    System.out.println();
    log("생산자 시작");
    for (int i = 1; i <= 3; i++) {
      Thread producer = new Thread(new ProducerTask(queue, "data" + i), "producer" + i);
      threads.add(producer);
      producer.start();
      sleep(100); // 1, 2, 3번을 순서대로 실행하려고 아주 잠깐 sleep
    }
  }

  private static void startConsumer(BoundedQueue queue, List<Thread> threads) {
    System.out.println();
    log("소비자 시작");
    for (int i = 1; i <= 3; i++) {
      Thread consumer = new Thread(new ConsumerTask(queue), "consumer" + i);
      threads.add(consumer);
      consumer.start();
      sleep(100);  // 1, 2, 3번을 순서대로 실행하려고 아주 잠깐 sleep
    }
  }

  private static void printAllState(BoundedQueue queue, List<Thread> threads) {
    System.out.println();
    log("현재 상태 출력, 큐 데이터: " + queue);
    for (Thread thread : threads) {
      log(thread.getName() + ": " + thread.getState());
    }
  }

}
