package thread.collection.simple;

import static util.ThreadUtils.sleep;

import java.util.Arrays;

public class SyncList implements SimpleList {

  private static final int DEFAULT_CAPACITY = 5;

  private Object[] elements;
  private int size;

  public SyncList() {
    this.elements = new Object[DEFAULT_CAPACITY];
    this.size = 0;
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public synchronized void add(Object e) {
    elements[size] = e;
    sleep(100); // 멀티스레드 문제를 발생시키는 코드
    size++;
  }

  @Override
  public synchronized Object get(int index) {
    return elements[index];
  }

  @Override
  public String toString() {
    return Arrays.toString(Arrays.copyOf(elements, size))
        + " size=" + size + ", capacity=" + elements.length;
  }
}
