package thread.cas.increment;

public class IncrementPerformanceMain {

  public static final long COUNT = 100_000_000L;

  public static void main(String[] args) {
    test(new BasicInteger());
    test(new VolatileInteger());
    test(new SyncInteger());
    test(new ReentrantLockInteger());
    test(new MyAtomicInteger());
  }

  /**
   * BasicInteger cost: 28ms
   * VolatileInteger cost: 116ms
   * SyncInteger cost: 381ms
   * ReentrantLockInteger cost: 383ms
   * MyAtomicInteger cost: 173ms
   */

  private static void test(IncrementInteger incrementInteger) {
    long start = System.currentTimeMillis();
    for (long i = 0; i < COUNT; i++) {
      incrementInteger.increment();
    }
    long end = System.currentTimeMillis();
    System.out.println(incrementInteger.getClass().getSimpleName() + " cost: " + (end - start) + "ms");
  }

}
