package thread.collection.java;

import static util.ThreadUtils.sleep;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 리스트 하나만 예시로 해보자
 */
public class ListMain {

  public static void main(String[] args) {
    List<Integer> syncList = new CopyOnWriteArrayList<>();
    List<Integer> noSyncList = new ArrayList<>();

    List<Thread> threads = new ArrayList<>();
    for (int i = 0; i < 10000; i++) {
      threads.add(new Thread(() -> {
        sleep(100);
        syncList.add(1);
        noSyncList.add(1);
      }));
    }

    threads.forEach(Thread::start);

    threads.forEach(t -> {
      try {
        t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    System.out.println("syncList.size() = " + syncList.size());
    System.out.println("noSyncList.size() = " + noSyncList.size());
  }

  /**
   * syncList.size() = 10000
   * noSyncList.size() = 9993
   */

}
