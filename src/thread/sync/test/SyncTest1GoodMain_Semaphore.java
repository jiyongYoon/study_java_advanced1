package thread.sync.test;

import static util.MyLogger.log;

import java.util.concurrent.Semaphore;

// Semaphore를 이용한 동기화 -> 접근 가능한 스레드를 1개로 제한하여 Mutex Lock과 같은 효과를 얻을 수 있다.
public class SyncTest1GoodMain_Semaphore {

  public static void main(String[] args) throws InterruptedException {
    Counter counter = new Counter();
    Runnable task = new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < 10000; i++) {
          try {
            counter.increment();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
        log("작업 완료!");
      }
    };

    Thread thread1 = new Thread(task);
    Thread thread2 = new Thread(task);

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

    System.out.println("결과: " + counter.getCount()); // 20000이 나와야 함
  }

  static class Counter {
    private int count = 0;
    private final Semaphore semaphore = new Semaphore(1);

    public void increment() throws InterruptedException {
      semaphore.acquire(); // 임계구역 설정
      count++;
      semaphore.release();
    }

    public int getCount() {
      return count;
    }
  }

}
