package thread.sync;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class BankMain {

  public static void main(String[] args) throws InterruptedException {
//    BankAccountV1 account = new BankAccountV1(1000);
//    BankAccountV2 account = new BankAccountV2(1000);
//    BankAccountV3 account = new BankAccountV3(1000);
//    BankAccountV4 account = new BankAccountV4(1000);
//    BankAccountV5 account = new BankAccountV5(1000);
    BankAccountV6 account = new BankAccountV6(1000);

    Thread t1 = new Thread(new WithdrawTask(account, 800), "t1");
    Thread t2 = new Thread(new WithdrawTask(account, 800), "t2");

    t1.start();
    t2.start();

    sleep(500);
    log("t1 state: " + t1.getState());
    log("t2 state: " + t2.getState());

    t1.join();
    t2.join();

    log("최종 잔액: " + account.getBalance());
  }

  /** BankAccountV1 (나는 두 스레드가 완전 동시에 실행되었을 때임)
   * -------------- t1, t2 동시 실행 --------------
   * 17:19:43.274 [       t1] 거래 시작: BankAccountV1
   * 17:19:43.274 [       t2] 거래 시작: BankAccountV1
   * 17:19:43.286 [       t1] [검증 시작] 출금액: 800, 현재잔액: 1000
   * 17:19:43.286 [       t2] [검증 시작] 출금액: 800, 현재잔액: 1000
   * 17:19:43.286 [       t1] [검증 완료] 출금액: 800, 현재잔액: 1000
   * 17:19:43.286 [       t2] [검증 완료] 출금액: 800, 현재잔액: 1000
   * 17:19:43.765 [     main] t1 state: TIMED_WAITING
   * 17:19:43.765 [     main] t2 state: TIMED_WAITING
   * 17:19:44.300 [       t1] [출금 완료] 출금액: 800, 변경잔액: 200
   * 17:19:44.302 [       t1] 거래 종료
   * 17:19:44.300 [       t2] [출금 완료] 출금액: 800, 변경잔액: 200
   * 17:19:44.304 [       t2] 거래 종료
   * 17:19:44.307 [     main] 최종 잔액: 200
   */

  /** BankAccountV2
   * -------------- t1 실행 --------------
   * 17:48:11.305 [       t1] 거래 시작: BankAccountV2
   * 17:48:11.314 [       t1] [검증 시작] 출금액: 800, 현재잔액: 1000
   * 17:48:11.315 [       t1] [검증 완료] 출금액: 800, 현재잔액: 1000
   * 17:48:11.797 [     main] t1 state: TIMED_WAITING
   * 17:48:11.797 [     main] t2 state: BLOCKED <--- BankAccountV2의 락이 없어서 synchronized 블록에 진입하지 못함
   * 17:48:12.317 [       t1] [출금 완료] 출금액: 800, 변경잔액: 200
   * 17:48:12.317 [       t1] 거래 종료
   * -------------- t2 실행 --------------
   * 17:48:12.318 [       t2] 거래 시작: BankAccountV2
   * 17:48:12.318 [       t2] [검증 시작] 출금액: 800, 현재잔액: 200
   * 17:48:12.318 [       t2] [검증 실패] 출금액: 800, 현재잔액: 200
   * 17:48:12.322 [     main] 최종 잔액: 200
   */

  /** BankAccountV3
   * -------------- t1, t2 동시 실행 --------------
   * 08:59:35.966 [       t1] 거래 시작: BankAccountV3
   * 08:59:35.966 [       t2] 거래 시작: BankAccountV3
   * -------------- t1 synchronized 블록 진입 --------------
   * 08:59:35.977 [       t1] [검증 시작] 출금액: 800, 현재잔액: 1000
   * 08:59:35.977 [       t1] [검증 완료] 출금액: 800, 현재잔액: 1000
   * 08:59:36.442 [     main] t1 state: TIMED_WAITING
   * 08:59:36.442 [     main] t2 state: BLOCKED
   * 08:59:36.978 [       t1] [출금 완료] 출금액: 800, 변경잔액: 200
   * 08:59:36.979 [       t1] 거래 종료
   * -------------- t1 synchronized 블록 탈출 --------------
   * -------------- t2 synchronized 블록 진입 --------------
   * 08:59:36.979 [       t2] [검증 시작] 출금액: 800, 현재잔액: 200
   * 08:59:36.980 [       t2] [검증 실패] 출금액: 800, 현재잔액: 200
   * 08:59:36.984 [     main] 최종 잔액: 200
   * -------------- t2 synchronized 블록 탈출 --------------
   */

  /** BankAccountV4
   * -------------- t1, t2 동시 실행 --------------> t1, t2 순서 보장하진 않음
   * 22:44:44.540 [       t1] 거래 시작: BankAccountV4
   * 22:44:44.540 [       t2] 거래 시작: BankAccountV4
   * 22:44:44.543 [       t1] [락 획득] t1
   * 22:44:44.547 [       t1] [검증 시작] 출금액: 800, 현재잔액: 1000
   * 22:44:44.547 [       t1] [검증 완료] 출금액: 800, 현재잔액: 1000
   * 22:44:45.030 [     main] t1 state: TIMED_WAITING
   * 22:44:45.030 [     main] t2 state: WAITING
   * 22:44:45.557 [       t1] [출금 완료] 출금액: 800, 변경잔액: 200
   * 22:44:45.557 [       t1] [락 반환] t1
   * 22:44:45.557 [       t2] [락 획득] t2
   * 22:44:45.558 [       t2] [검증 시작] 출금액: 800, 현재잔액: 200
   * 22:44:45.558 [       t1] 거래 종료
   * 22:44:45.558 [       t2] [검증 실패] 출금액: 800, 현재잔액: 200
   * 22:44:45.558 [       t2] [락 반환] t2
   * 22:44:45.560 [     main] 최종 잔액: 200
   */

  /** BankAccountV5
   * -------------- t1, t2 동시 실행 --------------> t1, t2 순서 보장하진 않음
   * 22:54:26.422 [       t1] 거래 시작: BankAccountV5
   * 22:54:26.422 [       t2] 거래 시작: BankAccountV5
   * 22:54:26.425 [       t1] [락 획득] t1
   * 22:54:26.425 [       t2] [진입 실패] 이미 처리중인 작업이 있습니다. <-- t2는 작업 끝
   * 22:54:26.429 [       t1] [검증 시작] 출금액: 800, 현재잔액: 1000
   * 22:54:26.429 [       t1] [검증 완료] 출금액: 800, 현재잔액: 1000
   * 22:54:26.909 [     main] t1 state: TIMED_WAITING
   * 22:54:26.909 [     main] t2 state: TERMINATED                  <-- t2 스레드 이미 끝남
   * 22:54:27.438 [       t1] [출금 완료] 출금액: 800, 변경잔액: 200
   * 22:54:27.438 [       t1] [락 반환] t1
   * 22:54:27.439 [       t1] 거래 종료
   * 22:54:27.440 [     main] 최종 잔액: 200
   */

  /**
   * -------------- t1, t2 동시 실행 --------------> t1, t2 순서 보장하진 않음
   * 23:05:18.029 [       t1] 거래 시작: BankAccountV6
   * 23:05:18.029 [       t2] 거래 시작: BankAccountV6
   * 23:05:18.032 [       t1] [락 획득] t1
   * 23:05:18.037 [       t1] [검증 시작] 출금액: 800, 현재잔액: 1000
   * 23:05:18.038 [       t1] [검증 완료] 출금액: 800, 현재잔액: 1000
   * 23:05:18.521 [     main] t1 state: TIMED_WAITING // sleep(1000) -> 출금작업 처리중임
   * 23:05:18.521 [     main] t2 state: TIMED_WAITING // tryLock(500) 으로 기다리는 중
   * 23:05:18.544 [       t2] [진입 실패] 이미 처리중인 작업이 있습니다. // 기다리다가 락 획득 못해서 작업 종료됨
   * 23:05:19.041 [       t1] [출금 완료] 출금액: 800, 변경잔액: 200
   * 23:05:19.041 [       t1] [락 반환] t1
   * 23:05:19.041 [       t1] 거래 종료
   * 23:05:19.042 [     main] 최종 잔액: 200
   */
}
