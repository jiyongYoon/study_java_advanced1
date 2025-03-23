package thread.start;

public class BadThreadMain {

  public static void main(String[] args) {
    System.out.println(Thread.currentThread().getName() + ": main() start");

    HelloThread helloThread = new HelloThread();
    System.out.println(Thread.currentThread().getName() + ": start() 호출 전");
    helloThread.run(); // run() 직접 실행
    System.out.println(Thread.currentThread().getName() + ": start() 호출 후");

    System.out.println(Thread.currentThread().getName() + ": main() end");
    /**
     * main: main() start
     * main: start() 호출 전
     * main: run() // main 스레드가 모든 메서드를 실행한다. 때문에 순서가 변하지 않는다.
     * main: start() 호출 후
     * main: main() end
     */
  }

}
