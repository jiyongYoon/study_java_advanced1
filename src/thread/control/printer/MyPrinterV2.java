package thread.control.printer;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyPrinterV2 {

  public static void main(String[] args) {
    Printer printer = new Printer();
    Thread printThread = new Thread(printer, "printer");
    printThread.start();

    Scanner userInput = new Scanner(System.in);
    while (true) {
      log("프린터 할 문서를 입력하세요. 종료 (q): ");
      String input = userInput.nextLine();
      if (input.equals("q")) {
        printer.work = false; // 없어도 됨
        printThread.interrupt();
        break;
      }

      printer.addJob(input);
    }
  }

  /**
   * 11:50:05.413 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 1
   * 11:50:06.583 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 11:50:06.589 [  printer] 출력 시작: 1, 대기 문서: []
   * 2
   * 11:50:06.860 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 3
   * 11:50:07.143 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 4
   * 11:50:07.406 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 5
   * 11:50:07.660 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 11:50:09.602 [  printer] 출력 완료: 1
   * 11:50:09.602 [  printer] 출력 시작: 2, 대기 문서: [3, 4, 5]
   * q
   * 11:50:11.307 [  printer] 프린터 종료
   */

  static class Printer implements Runnable {
    volatile boolean work = true; // 없어도 됨
    Queue<String> jobQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
      while (work) {
        if (jobQueue.isEmpty()) {
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
