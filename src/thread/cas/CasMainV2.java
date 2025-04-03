package thread.cas;

import static util.MyLogger.log;

import java.util.concurrent.atomic.AtomicInteger;

public class CasMainV2 {

  public static void main(String[] args) {
    AtomicInteger atomicInteger = new AtomicInteger(0);
    System.out.println("start value = " + atomicInteger.get());

    int resultValue1 = incrementAndGet(atomicInteger);
    System.out.println("resultValue1 = " + resultValue1);

    int resultValue2 = incrementAndGet(atomicInteger);
    System.out.println("resultValue2 = " + resultValue2);
  }

  /**
   * start value = 0
   * 21:49:03.915 [     main] getValue = 0
   * 21:49:03.919 [     main] result = true
   * resultValue1 = 1
   * 21:49:03.919 [     main] getValue = 1
   * 21:49:03.919 [     main] result = true
   * resultValue2 = 2
   */

  private static int incrementAndGet(AtomicInteger atomicInteger) {
    int getValue;
    boolean result;
    do {
      getValue = atomicInteger.get();
      log("getValue = " + getValue);
      result = atomicInteger.compareAndSet(getValue, getValue + 1); // 여기서 CAS 연산을 사용한다.
      log("result = " + result);
      // 만약 다른 스레드가 값을 변경했다면, 위 작업을 그대로 다시 실행
    } while (!result);
    return getValue + 1; // atomicInteger.get();은 그 사이에 또 다른 스레드가 값을 변경할 수 있으므로 getValue + 1 자체를 반환한다.
  }
}