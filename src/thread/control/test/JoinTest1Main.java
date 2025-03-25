package thread.control.test;

import static util.MyLogger.log;

import util.ThreadUtils;

public class JoinTest1Main {

  public static void main(String[] args) throws InterruptedException {
    Thread t1 = new Thread(new MyTask(), "t1");
    Thread t2 = new Thread(new MyTask(), "t2");
    Thread t3 = new Thread(new MyTask(), "t3");

    t1.start(); // 3초
    t1.join();

    t2.start(); // 3초
    t2.join();

    t3.start(); // 3초
    t3.join();

    log("모든 스레드 실행 완료");
  }

  /**
   * 09:14:07.406 [       t1] 1
   * 09:14:08.423 [       t1] 2
   * 09:14:09.438 [       t1] 3
   * 09:14:10.451 [       t2] 1
   * 09:14:11.459 [       t2] 2
   * 09:14:12.469 [       t2] 3
   * 09:14:13.475 [       t3] 1
   * 09:14:14.488 [       t3] 2
   * 09:14:15.497 [       t3] 3
   * 09:14:16.501 [     main] 모든 스레드 실행 완료
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
