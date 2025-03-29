package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccountV4 implements BankAccount {

  private int balance;

  // 비공정 모드
  private final Lock lock = new ReentrantLock();

  public BankAccountV4(int initialBalance) {
    this.balance = initialBalance;
  }

  @Override
  public boolean withdraw(int amount) {
    log("거래 시작: " + getClass().getSimpleName());

    lock.lock();
    log("[락 획득] " + Thread.currentThread().getName());

    try {
      log("[검증 시작] 출금액: " + amount + ", 현재잔액: " + balance);
      if (balance < amount) {
        log("[검증 실패] 출금액: " + amount + ", 현재잔액: " + balance);
        return false;
      } else {
        log("[검증 완료] 출금액: " + amount + ", 현재잔액: " + balance);
        sleep(1000); // 출금에 걸리는 시간
        balance -= amount;
        log("[출금 완료] 출금액: " + amount + ", 변경잔액: " + balance);
      }
    } finally {
      // 반드시 호출 필요!!
      log("[락 반환] " + Thread.currentThread().getName());
      lock.unlock();
    }

    log("거래 종료");
    return true;
  }

  @Override
  public int getBalance() {
    lock.lock();
    try {
      return balance;
    } finally {
      lock.unlock();
    }
  }
}
