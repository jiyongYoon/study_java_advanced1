package thread.control;

import static util.MyLogger.log;

import thread.start.HelloRunnable;

public class ThreadInfoMain {

  public static void main(String[] args) {
    Thread mainThread = Thread.currentThread();
    log("mainThread = " + mainThread);
//    log("mainThread.threadId() = " + mainThread.threadId()); // intellij랑 java버전때문에 21->17로 내리면서 메서드 못씀
    log("mainThread.getName() = " + mainThread.getName());
    log("mainThread.getPriority() = " + mainThread.getPriority()); // 1~10, 기본값 5, 우선순위가 높을수록 좀 더 자주 실행될 가능성이 높아짐.
    log("mainThread.getThreadGroup() = " + mainThread.getThreadGroup());
    log("mainThread.getState() = " + mainThread.getState());

    Thread myThread = new Thread(new HelloRunnable(), "myThread");
    log("myThread = " + myThread);
//    log("myThread.threadId() = " + myThread.threadId());
    log("myThread.getName() = " + myThread.getName());
    log("myThread.getPriority() = " + myThread.getPriority()); // 1~10, 기본값 5, 우선순위가 높을수록 좀 더 자주 실행될 가능성이 높아짐.
    log("myThread.getThreadGroup() = " + myThread.getThreadGroup());
    log("myThread.getState() = " + myThread.getState());
    myThread.start();
    log("myThread.getState() = " + myThread.getState());
  }

  /**
   * 16:46:23.989 [     main] mainThread = Thread[#1,main,5,main] // 스레드ID(jvm 할당, 스레드 식별자로 유일), 스레드이름, 우선순위, 스레드그룹 순서
   * 16:46:23.999 [     main] mainThread.threadId() = 1
   * 16:46:24.000 [     main] mainThread.getName() = main
   * 16:46:24.005 [     main] mainThread.getPriority() = 5
   * 16:46:24.006 [     main] mainThread.getThreadGroup() = java.lang.ThreadGroup[name=main,maxpri=10]
   * 16:46:24.006 [     main] mainThread.getState() = RUNNABLE // 실행됨
   * 16:46:24.007 [     main] myThread = Thread[#30,myThread,5,main]
   * 16:46:24.007 [     main] myThread.threadId() = 30
   * 16:46:24.007 [     main] myThread.getName() = myThread
   * 16:46:24.008 [     main] myThread.getPriority() = 5
   * 16:46:24.008 [     main] myThread.getThreadGroup() = java.lang.ThreadGroup[name=main,maxpri=10]
   * 16:46:24.008 [     main] myThread.getState() = NEW // 생성하고 아직 실행안함
   * 16:46:24.008 [     main] myThread.getState() = RUNNABLE // 실행됨
   * myThread: run()
   */
}
