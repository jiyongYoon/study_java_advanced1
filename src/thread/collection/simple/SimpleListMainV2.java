package thread.collection.simple;

import static util.MyLogger.log;

public class SimpleListMainV2 {

  public static void main(String[] args) throws InterruptedException {
//    test(new BasicList());
//    test(new SyncList());
    test(new SyncProxyList(new BasicList())); // 동기화 프록시
  }

  public static void test(SimpleList list) throws InterruptedException {
    log(list.getClass().getSimpleName());

    Thread threadA = new Thread(new Runnable() {
      @Override
      public void run() {
        list.add("A");
        log("Thread-1: list.add(A)");
      }
    }, "Thread-1");

    Thread threadB = new Thread(new Runnable() {
      @Override
      public void run() {
        list.add("B");
        log("Thread-2: list.add(B)");
      }
    }, "Thread-2");

    threadA.start();
    threadB.start();

    threadA.join();
    threadB.join();

    System.out.println("list = " + list);
  }

  /** BasicList
   * 21:46:14.734 [     main] BasicList
   * 21:46:14.852 [ Thread-2] Thread-2: list.add(B)
   * 21:46:14.852 [ Thread-1] Thread-1: list.add(A)
   * list = [B] size=1, capacity=5
   *
   * 21:46:34.625 [     main] BasicList
   * 21:46:34.732 [ Thread-1] Thread-1: list.add(A)
   * 21:46:34.732 [ Thread-2] Thread-2: list.add(B)
   * list = [B, null] size=2, capacity=5
   */

  /** SyncList
   * 21:51:52.117 [     main] SyncList
   * 21:51:52.232 [ Thread-1] Thread-1: list.add(A)
   * 21:51:52.342 [ Thread-2] Thread-2: list.add(B)
   * list = [A, B] size=2, capacity=5
   */

  /**
   * SyncProxyList
   * 21:56:43.437 [     main] SyncProxyList
   * 21:56:43.553 [ Thread-1] Thread-1: list.add(A)
   * 21:56:43.658 [ Thread-2] Thread-2: list.add(B)
   * list = [A, B] size=2, capacity=5 by SyncProxyList
   */

}
