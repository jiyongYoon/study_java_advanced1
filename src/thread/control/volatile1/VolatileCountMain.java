package thread.control.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class VolatileCountMain {

  public static void main(String[] args) {
    MyTask myTask = new MyTask();
    Thread thread = new Thread(myTask, "work");
    thread.start();

    sleep(1000);

    myTask.flag = false;
    log("flag = " + myTask.flag + ", count = " + myTask.count + " in main()");
  }

  /**
   * 16:41:16.709 [     work] flag =  true, count = 100000000 in while()
   * 16:41:17.001 [     work] flag =  false, count = 142200126 종료
   * 16:41:17.001 [     main] flag = false, count = 142200126 in main() <-- 정확하게 멈추기도 했는데, 현저하게 느려진게 느껴진다 ㅋㅋㅋ
   */

  static class MyTask implements Runnable {

//    boolean flag = true;
//    long count;

    volatile boolean flag = true;
    volatile long count;

    @Override
    public void run() {
      while (flag) {
        count++;
        // 1억번에 한번씩 출력
        if (count % 100_000_000 == 0) {
          log("flag =  " + flag + ", count = " + count + " in while()");
        }
      }
      log("flag =  " + flag + ", count = " + count + " 종료");
    }
  }

}
