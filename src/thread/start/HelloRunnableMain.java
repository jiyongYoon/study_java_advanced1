package thread.start;

/**
 * 실무에서는 거의 이런식으로 사용함
 *
 * 장-단점이 있는데,결국 Thread는 '상속'이고 Runnable은 '구현'이기 때문에, 해당 장-단점이 그대로 적용된다.
 */
public class HelloRunnableMain {

  public static void main(String[] args) {
    System.out.println(Thread.currentThread().getName() + ": main() start");

    HelloRunnable runnable = new HelloRunnable();
    Thread thread = new Thread(runnable);
    thread.start();

    System.out.println(Thread.currentThread().getName() + ": main() end");
  }

  /**
   * main: main() start
   * main: main() end
   * Thread-0: run()
   */

}
