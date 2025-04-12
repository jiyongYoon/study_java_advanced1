package thread.executor.future;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.Random;

public class RunnableMain {

  public static void main(String[] args) throws InterruptedException {
    MyRunnable task = new MyRunnable();
    Thread thread = new Thread(task, "Thread-1");
    thread.start();
    thread.join();
    int result = task.value;
    log("result value = " + result);
  }

  /**
   * 22:36:16.710 [ Thread-1] Runnable 시작
   * 22:36:18.717 [ Thread-1] create value = 7
   * 22:36:18.717 [ Thread-1] Runnable 완료
   * 22:36:18.717 [     main] result value = 7
   */

  static class MyRunnable implements Runnable {
    int value; // runnable이 리턴값이 없기 때문에 필드에서 값을 가져와야 한다.

    @Override
    public void run() {
      log("Runnable 시작");
      sleep(2000);
      value = new Random().nextInt(10);
      log("create value = " + value);
      log("Runnable 완료");
    }
  }

}
