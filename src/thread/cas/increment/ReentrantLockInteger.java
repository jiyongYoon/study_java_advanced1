package thread.cas.increment;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockInteger implements IncrementInteger {

  private int value;
  private final Lock lock = new ReentrantLock();

  @Override
  public void increment() {
    lock.lock();
    try {
      value++;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int get() {
    return this.value;
  }
}
