package thread.control;

import static util.MyLogger.log;

public class ThreadStateMain {

  public static void main(String[] args) throws InterruptedException {
    Thread myThread = new Thread(new MyRunnable(), "myThread");
    log("myThread.state1 = " + myThread.getState()); // NEW
    log("myThread.start");
    myThread.start();
    Thread.sleep(1000);
    log("myThread.state3 = " + myThread.getState()); // TIMED_WAITING
    Thread.sleep(4000);
    log("myThread.state5 = " + myThread.getState()); // TERMINATED
    log("end");
  }

  static class MyRunnable implements Runnable {

    @Override
    public void run() {
      log("start");
      log("myThread.state2 = " + Thread.currentThread().getState()); // RUNNABLE
      log("sleep() start");
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      log("sleep() end");
      log("myThead.state4 = " + Thread.currentThread().getState()); // RUNNABLE
      log("end");
    }
  }

  /**
   * 17:12:35.537 [     main] myThread.state1 = NEW
   * 17:12:35.542 [     main] myThread.start
   * 17:12:35.542 [ myThread] start
   * 17:12:35.543 [ myThread] myThread.state2 = RUNNABLE
   * 17:12:35.543 [ myThread] sleep() start
   * 17:12:36.550 [     main] myThread.state3 = TIMED_WAITING
   * 17:12:38.552 [ myThread] sleep() end
   * 17:12:38.552 [ myThread] myThead.state4 = RUNNABLE
   * 17:12:38.553 [ myThread] end
   * 17:12:40.560 [     main] myThread.state5 = TERMINATED
   * 17:12:40.561 [     main] end
   */

}
