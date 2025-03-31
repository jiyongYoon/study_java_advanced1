package thread.bounded;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.ArrayDeque;
import java.util.Queue;

/** Object.wait(), Object.notify() 사용 시
 * wait()로 대기하여 순서를 돌릴수는 있으나,
 * notify()시 깨어난 스레드가 바로 작업을 할 수 있는 상황이 아닐 수 있다는 비효율이 발생한다. (한계점)
 *
 * => 즉, 대기하는 공간에 생산자-소비자가 구분되지 않는다는 점이 문제점이다.
 * 대기 공간이 분류되어 나누어져있다면, 작업을 할 수 있는 스레드를 정확하게 깨울 수 있을 것이다!
 */
public class BoundedQueueV3 implements BoundedQueue {

  // 핵심 공유자원
  private final Queue<String> queue = new ArrayDeque<>();
  private final int max;

  public BoundedQueueV3(int max) {
    this.max = max;
  }

  @Override
  public synchronized void put(String data) {
    while (queue.size() == max) {
      log("[put] 큐가 가득 참, 생산자 대기");
      try {
        wait(); // RUNNABLE -> WAITING, 락 반납
        log("[put] 생산자 깨어남");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    queue.offer(data);
    log("[put] 생산자 데이터 저장, notify() 호출");
    notify(); // 대기 스레드, WAIT -> BLOCKED
    // notifyAll(); // 모든 대기 스레드, WAIT -> BLOCKED
  }

  @Override
  public synchronized String take() {
    while (queue.isEmpty()) {
      log("[take] 큐에 데이터가 없음, 소비자 대기");
      try {
        wait();
        log("[take] 소비자 깨어남");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    String data = queue.poll();
    log("[take] 소비자 데이터 획득, notify() 호출");
    notify(); // 대기 스레드, WAIT -> BLOCKED
//    notifyAll(); // 모든 대기 스레드, WAIT -> BLOCKED

    return data;
  }

  @Override
  public String toString() {
    return queue.toString();
  }

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
