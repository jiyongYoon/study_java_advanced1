package util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 추상클래스로 만들어서 직접 생성할 수 없도록 함
 */
public abstract class MyLogger {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

  public static void log(Object obj) {
    String time = LocalTime.now().format(formatter);
    System.out.printf("%s [%9s] %s\n", time, Thread.currentThread().getName(), obj);
  }

}
