package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankAccountV1 implements BankAccount {

  private int balance;
//  volatile private int balance;


  public BankAccountV1(int initialBalance) {
    this.balance = initialBalance;
  }

  @Override
  public boolean withdraw(int amount) {
    log("거래 시작: " + getClass().getSimpleName());

    log("[검증 시작] 출금액: " + amount + ", 현재잔액: " + balance);
    if (balance < amount) {
      log("[검증 실패] 출금액: " + amount + ", 현재잔액: " + balance);
      return false;
    } else {
      log("[검증 완료] 출금액: " + amount + ", 현재잔액: " + balance);
      sleep(1000); // 출금에 걸리는 시간
      balance -= amount;
      log("[출금 완료] 출금액: " + amount + ", 변경잔액: " + balance);

      log("거래 종료");
      return true;
    }
  }

  @Override
  public int getBalance() {
    return balance;
  }
}
