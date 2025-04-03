package thread.cas.spinlock;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.atomic.AtomicBoolean;

public class SpinLock {

  private final AtomicBoolean lock = new AtomicBoolean(false);

  public void lock() {
    log("락 획득 시도");
    while (!lock.compareAndSet(false, true)) { // CAS 원자적 연산 사용
      log("락 획득 실패 - 스핀 대기");
    }
    log("락 획득 성공");
  }

  public void unlock() {
    lock.set(false);
    log("락 반납 완료");
  }

}
