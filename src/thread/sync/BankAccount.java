package thread.sync;

public interface BankAccount {

  // 계좌의 돈을 출금한다
  boolean withdraw(int amount);

  // 계좌의 잔액을 반환한다
  int getBalance();

}
