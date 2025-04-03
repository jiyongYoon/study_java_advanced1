package thread.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS 메서드는 원자적으로 실행됨
 * -> 소프트웨어적으로 구현한게 아니라 현대에는 CPU에서 CAS 연산을 지원함
 */
public class CasMainV1 {

  public static void main(String[] args) {
    AtomicInteger atomicInteger = new AtomicInteger(0);
    System.out.println("start value = " + atomicInteger.get());

    // 값이 0이면 1로 세팅해 -> true -> 1로 세팅함
    boolean result1 = atomicInteger.compareAndSet(0, 1);
    System.out.println("result1 = " + result1 + ", value = " + atomicInteger.get());

    // 값이 0이면 2로 세팅해 -> false -> 1로 세팅하지 않음
    boolean result2 = atomicInteger.compareAndSet(0, 2);
    System.out.println("result2 = " + result2 + ", value = " + atomicInteger.get());
  }

  /**
   * start value = 0
   * result1 = true, value = 1
   * result2 = false, value = 1
   */
}
