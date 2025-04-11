package thread.collection.simple;

public class SimpleListMainV1 {

  public static void main(String[] args) {
    BasicList list = new BasicList();
    list.add("A");
    list.add("B");
    System.out.println("list = " + list);
  }

  /** 문제 없음
   * list = [A, B] size=2, capacity=5
   */

}
