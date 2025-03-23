package thread.start;

/**
 * jvm은 데몬 스레드 동작을 기다리지 않고 사용자 스레드가 끝나면 프로그램을 닫는다. (끝난다)
 */
public class DaemonThreadMain {

  public static void main(String[] args) {
    System.out.println(Thread.currentThread().getName() + ": main() start");

    DaemonThread daemonThread = new DaemonThread();
    daemonThread.setDaemon(true); // 데몬 스레드 O
//    daemonThread.setDaemon(false); // 데몬 스레드 X
    daemonThread.start();

    System.out.println(Thread.currentThread().getName() + ": main() end");
  }

  /** setDaemon(true); --> Thread-0 이 데몬 스레드임
   * main: main() start
   * main: main() end
   * Thread-0: run() start
   * ------------------------
   * Thread-0: run() end 가 출력되기 전에 jvm 프로그램이 종료되었다.
   */

  /** setDaemon(false); --> Thread-0 이 사용자 스레드임
   * main: main() start
   * main: main() end
   * Thread-0: run() start
   * (10초 후)
   * Thread-0: run() end
   */

  static class DaemonThread extends Thread {

    @Override
    public void run() {
//    public void run() throws Exception { // checked 예외는 무조건 내부에서 잡아야 함
      System.out.println(Thread.currentThread().getName() + ": run() start");

      try {
        Thread.sleep(10000); // 10초간
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      System.out.println(Thread.currentThread().getName() + ": run() end");
    }
  }

}
