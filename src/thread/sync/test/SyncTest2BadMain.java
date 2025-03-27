package thread.sync.test;

import static util.MyLogger.log;

import thread.control.CheckedExceptionMain.Runnable;

// localValue의 지역변수에 동시성 문제가 발생하는지 하지 않는지 생각해보자
// -> count() 메서드는 같은 myCounter 객체로 참조하지만, 그 메서드를 호출한 각 스레드에 스택영역 안에 변수가 생기므로
//    동시성 문제가 발생하지 않는다. 지역변수는 해당 스레드만 접근이 가능하다.
public class SyncTest2BadMain {

  public static void main(String[] args) throws InterruptedException {
    MyCounter myCounter = new MyCounter();
    Runnable task = new Runnable() {
      @Override
      public void run() {
        myCounter.count();
      }
    };

    Thread thread1 = new Thread(task, "Thread-1");
    Thread thread2 = new Thread(task, "Thread-2");

    thread1.start();
    thread2.start();
  }

  static class MyCounter {
    public void count() {
      int localValue = 0;
      for (int i = 0; i < 1000; i++) {
        localValue++;
      }
      log("결과: " + localValue);
    }
  }

}
