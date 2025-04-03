package thread.cas.spinlock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class SpinLockBad {
  private volatile boolean lock = false;

  public void lock() {
    log("락 획득 시도");
    while (true) {
      // 1. 락 사용여부 확인과 2. 락 변경이 원자적으로 실행되지 않기 때문에 락의 임계영역이 지켜지지 않는다.
      if (!lock) { // 1. 락 사용 여부 확인
        sleep(100); // 문제 상황 확인용, 스레드 대기
        lock = true; // 2. 락 변경
        break; // while 탈출
      } else {
        // 락을 획득할 때까지 스핀 대기(바쁜 대기) 한다.
        log("락 획득 실패 - 스핀 대기");
      }
    }
    log("락 획득 성공");
  }

  public void unlock() {
    lock = false; // 이건 원자적인 연산
    log("락 반납 완료");
  }

}
