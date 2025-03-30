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
}
