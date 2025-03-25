package thread.control.join;

import static util.MyLogger.log;

import util.ThreadUtils;

public class JoinMainV0 {

  public static void main(String[] args) {
    log("start");

    Thread thread1 = new Thread(new Job(), "thread-1");
    Thread thread2 = new Thread(new Job(), "thread-2");

    thread1.start();
    thread2.start();

    log("end");
  }

  /**
   * 17:37:16.583 [     main] start
   * 17:37:16.601 [     main] end
   * 17:37:16.601 [ thread-2] 작업 시작
   * 17:37:16.601 [ thread-1] 작업 시작
   * 17:37:18.627 [ thread-1] 작업 끝
   * 17:37:18.627 [ thread-2] 작업 끝
   */

  static class Job implements Runnable {

    @Override
    public void run() {
      log("작업 시작");
      ThreadUtils.sleep(2000);
      log("작업 끝");
    }
  }
}
