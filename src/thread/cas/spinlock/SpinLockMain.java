package thread.cas.spinlock;

import static util.MyLogger.log;

public class SpinLockMain {

  public static void main(String[] args) {
//    SpinLockBad spinLock = new SpinLockBad();
    SpinLock spinLock = new SpinLock();

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        spinLock.lock();
        try {
          // critical section
          log("비즈니스 로직 실행 중...");
          // sleep(1); // 오래 걸리는 로직에서 스핀 락 사용 X
        } finally {
          spinLock.unlock();
        }
      }
    };

    Thread thread1 = new Thread(runnable, "Thread-1");
    Thread thread2 = new Thread(runnable, "Thread-2");

    thread1.start();
    thread2.start();
  }

  /** SpinLockBad()
   * 22:14:57.992 [ Thread-2] 락 획득 시도           --> 두 스레드가 동시에 false를 읽기 때문에 진입에 성공해버린다.
   * 22:14:57.992 [ Thread-1] 락 획득 시도           --> 두 스레드가 동시에 false를 읽기 때문에 진입에 성공해버린다.
   * 22:14:58.099 [ Thread-2] 락 획득 성공
   * 22:14:58.099 [ Thread-1] 락 획득 성공
   * 22:14:58.099 [ Thread-2] 비즈니스 로직 실행 중...
   * 22:14:58.099 [ Thread-1] 비즈니스 로직 실행 중...
   * 22:14:58.099 [ Thread-2] 락 반납 완료
   * 22:14:58.099 [ Thread-1] 락 반납 완료
   */

  /** SpinLock()
   * 22:22:49.557 [ Thread-2] 락 획득 시도           --> 두 스레드가 동시에 락 획득을 시도하나
   * 22:22:49.557 [ Thread-1] 락 획득 시도           --> 두 스레드가 동시에 락 획득을 시도하나
   * 22:22:49.559 [ Thread-1] 락 획득 성공           --> Thread-1이 먼저 성공
   * 22:22:49.559 [ Thread-2] 락 획득 실패 - 스핀 대기
   * 22:22:49.559 [ Thread-1] 비즈니스 로직 실행 중...
   * 22:22:49.559 [ Thread-2] 락 획득 실패 - 스핀 대기
   * 22:22:49.560 [ Thread-1] 락 반납 완료           --> Thread-1이 먼저 락 반납
   * 22:22:49.560 [ Thread-2] 락 획득 성공           --> Thread-2가 락 획득 성공
   * 22:22:49.560 [ Thread-2] 비즈니스 로직 실행 중...
   * 22:22:49.560 [ Thread-2] 락 반납 완료           --> Thread-2가 락 반납
   */
}
