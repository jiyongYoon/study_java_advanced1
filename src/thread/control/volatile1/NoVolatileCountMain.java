package thread.control.volatile1;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

public class NoVolatileCountMain {

  public static void main(String[] args) {
    MyTask myTask = new MyTask();
    Thread thread = new Thread(myTask, "work");
    thread.start();

    sleep(1000);

    myTask.flag = false;
    log("flag = " + myTask.flag + ", count = " + myTask.count + " in main()");
  }

  /**
   * 16:33:48.149 [     work] flag =  true, count = 100000000 in while()
   * 16:33:48.278 [     work] flag =  true, count = 200000000 in while()
   * 16:33:48.394 [     work] flag =  true, count = 300000000 in while()
   * 16:33:48.512 [     work] flag =  true, count = 400000000 in while()
   * 16:33:48.637 [     work] flag =  true, count = 500000000 in while()
   * 16:33:48.763 [     work] flag =  true, count = 600000000 in while()
   * 16:33:48.880 [     work] flag =  true, count = 700000000 in while()
   * 16:33:48.993 [     work] flag =  true, count = 800000000 in while()
   * 16:33:49.024 [     main] flag = false, count = 829817241 in main() // main thread가 work thread의 flag를 false로 변경시켰으나, 캐시에서만 동작함
   * 16:33:49.095 [     work] flag =  true, count = 900000000 in while() // 그래서 work thread가 계속 동작하다가, 이 시점에서 main thread의 캐시에서 힙메모리로 동기화된 flag 데이터가 work thread의 flag에 동기화 되며
   * 16:33:49.096 [     work] flag = false, count = 900000000 종료 // while문을 탈출하여 멈추게 됨
   */

  static class MyTask implements Runnable {

    boolean flag = true;
    long count;

//    volatile boolean flag = true;
//    volatile long count;

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
