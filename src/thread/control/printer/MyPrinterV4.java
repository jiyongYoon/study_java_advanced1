package thread.control.printer;

import static util.MyLogger.log;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyPrinterV4 {

  public static void main(String[] args) {
    Printer printer = new Printer();
    Thread printThread = new Thread(printer, "printer");
    printThread.start();

    Scanner userInput = new Scanner(System.in);
    while (true) {
      log("프린터 할 문서를 입력하세요. 종료 (q): ");
      String input = userInput.nextLine();
      if (input.equals("q")) {
        printThread.interrupt();
        break;
      }

      printer.addJob(input);
    }
  }

  /**
   * 11:53:03.603 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 1
   * 11:53:05.216 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 11:53:05.223 [  printer] 출력 시작: 1, 대기 문서: []
   * 2
   * 11:53:05.429 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 3
   * 11:53:05.627 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 4
   * 11:53:05.818 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 5
   * 11:53:06.057 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * q
   * 11:53:06.840 [  printer] 프린터 종료
   */

  static class Printer implements Runnable {
    Queue<String> jobQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
      while (!Thread.interrupted()) {
        // 이 부분에 cpu가 계속 사용된다.
        if (jobQueue.isEmpty()) {
          Thread.yield(); // 추가
          continue;
        }

        String job = jobQueue.poll();
        log("출력 시작: " + job + ", 대기 문서: " + jobQueue);
        try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          break;
        }
        log("출력 완료: " + job);
      }
      log("프린터 종료");
    }

    public void addJob(String input) {
      jobQueue.offer(input);
    }
  }
}
