package thread.start;

public class HelloThreadMain {

  public static void main(String[] args) {
    System.out.println(Thread.currentThread().getName() + ": main() start");

    HelloThread helloThread = new HelloThread();
    System.out.println(Thread.currentThread().getName() + ": start() 호출 전");
    helloThread.start();
    /*
     helloThread.run()을 직접 호출하지 않는다.
     start() 메서드를 호출해야 새로운 스레드를 생성해서 스택 공간을 할당 하고 동작한다.

     main 스레드는 helloThread에게 작업을 지시한 후 관심없이 다음 코드로 넘어간다.
     */
    System.out.println(Thread.currentThread().getName() + ": start() 호출 후");

    System.out.println(Thread.currentThread().getName() + ": main() end");
    /**
     * main: main() start
     * main: start() 호출 전
     * main: start() 호출 후
     * main: main() end
     * Thread-0: run() // 이 위치는 컨텍스트 스위칭에 따라 달라질 수 있다.
     */
  }

}
