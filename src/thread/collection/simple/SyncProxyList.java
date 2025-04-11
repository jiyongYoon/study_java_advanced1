package thread.collection.simple;

public class SyncProxyList implements SimpleList {

  private final SimpleList target;

  // SimpleList 구현체를 받기 때문에 SimpleList를 구현한 구현체는 모두 프록시를 사용해 동기화 기능을 수행할 수 있다.
  public SyncProxyList(SimpleList list) {
    this.target = list;
  }

  @Override
  public synchronized int size() {
    return target.size();
  }

  @Override
  public synchronized void add(Object e) {
    target.add(e);
  }

  @Override
  public synchronized Object get(int index) {
    return target.get(index);
  }

  @Override
  public synchronized String toString() {
    return target.toString() + " by " + this.getClass().getSimpleName();
  }
}
