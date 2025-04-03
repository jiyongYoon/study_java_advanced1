package thread.cas;

import static util.MyLogger.log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CasMainV3 {

  private static final int THREAD_COUNT = 2;

  public static void main(String[] args) throws InterruptedException {
    AtomicInteger atomicInteger = new AtomicInteger(0);
    System.out.println("start value = " + atomicInteger.get());

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        incrementAndGet(atomicInteger);
      }
    };

    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < THREAD_COUNT; i++) {
      Thread thread = new Thread(runnable);
      threads.add(thread);
      thread.start();
    }

    for (Thread thread : threads) {
      thread.join();
    }

    int result = atomicInteger.get();
    System.out.println(atomicInteger.getClass().getSimpleName() + " - resultValue: " + result);
  }

  /**
   * start value = 0
   * 21:55:47.503 [ Thread-1] getValue = 0   <--- 두 스레드 동시에 0을 읽음
   * 21:55:47.503 [ Thread-0] getValue = 0   <--- 두 스레드 동시에 0을 읽음
   * 21:55:47.507 [ Thread-1] result = true  <--- Thread-1이 성공함
   * 21:55:47.507 [ Thread-0] result = false <--- Thread-0은 한번 실패함
   * 21:55:47.507 [ Thread-0] getValue = 1   <--- 다시 값을 읽음
   * 21:55:47.507 [ Thread-0] result = true  <--- 성공함
   * AtomicInteger - resultValue: 2          <--- 최종 결과
   */

  private static int incrementAndGet(AtomicInteger atomicInteger) {
    int getValue;
    boolean result;
    do {
      getValue = atomicInteger.get();
      log("getValue = " + getValue);
      result = atomicInteger.compareAndSet(getValue, getValue + 1);
      log("result = " + result);
    } while (!result);
    return getValue + 1;
  }
}