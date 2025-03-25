package thread.control.printer;

import static util.MyLogger.log;
import static util.ThreadUtils.*;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import util.ThreadUtils;

public class MyPrinterV1 {

  public static void main(String[] args) {
    Printer printer = new Printer();
    Thread printThread = new Thread(printer, "printer");
    printThread.start();

    Scanner userInput = new Scanner(System.in);
    while (true) {
      log("프린터 할 문서를 입력하세요. 종료 (q): ");
      String input = userInput.nextLine();
      if (input.equals("q")) {
        printer.work = false;
        break;
      }

      printer.addJob(input);
    }
  }

  /**
   * 11:50:58.600 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 1
   * 11:51:00.263 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 11:51:00.269 [  printer] 출력 시작: 1, 대기 문서: []
   * 2
   * 11:51:00.514 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 3
   * 11:51:00.758 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 4
   * 11:51:00.958 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * 5
   * 11:51:01.163 [     main] 프린터 할 문서를 입력하세요. 종료 (q):
   * q
   * 11:51:03.270 [  printer] 출력 완료: 1 <-- 바로 반응하지 않고 하던 작업은 함
   * 11:51:03.270 [  printer] 프린터 종료
   */

  static class Printer implements Runnable {

    volatile boolean work = true;
    Queue<String> jobQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
      while (work) {
        if (jobQueue.isEmpty()) {
          continue;
        }

        String job = jobQueue.poll();
        log("출력 시작: " + job + ", 대기 문서: " + jobQueue);
        sleep(3000);
        log("출력 완료: " + job);
      }
      log("프린터 종료");
    }

    public void addJob(String input) {
      jobQueue.offer(input);
    }
  }
}
