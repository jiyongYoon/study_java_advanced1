package thread.control.test;

import static util.MyLogger.log;

import util.ThreadUtils;

public class JoinTest2Main {

  public static void main(String[] args) throws InterruptedException {
    Thread t1 = new Thread(new MyTask(), "t1");
    Thread t2 = new Thread(new MyTask(), "t2");
    Thread t3 = new Thread(new MyTask(), "t3");

    // 병렬로 처리
    t1.start(); // 3초
    t2.start(); // 3초
    t3.start(); // 3초

    t1.join();
    t2.join();
    t3.join();

    log("모든 스레드 실행 완료");
  }

  /**
   * 09:15:14.379 [       t2] 1
   * 09:15:14.379 [       t1] 1
   * 09:15:14.379 [       t3] 1
   * 09:15:15.395 [       t1] 2
   * 09:15:15.395 [       t3] 2
   * 09:15:15.395 [       t2] 2
   * 09:15:16.406 [       t1] 3
   * 09:15:16.406 [       t2] 3
   * 09:15:16.406 [       t3] 3
   * 09:15:17.416 [     main] 모든 스레드 실행 완료
   */
  static class MyTask implements Runnable {

    @Override
    public void run() {
      for (int i = 1; i <= 3; i++) {
        log(i);
        ThreadUtils.sleep(1000);
      }
    }
  }

}
