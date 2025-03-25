package thread.control;

public class CheckedExceptionMain {

  public static void main(String[] args) throws Exception {
    throw new Exception();
  }

  /* 불가능
  static class CheckedRunnable implements Runnable {

    @Override
    public void run() throws Exception {
      throw new Exception();
    }
  }
  */

  /** java의 체크예외 throw 규칙!!
   * 1) Override하는 메서드가 체크 예외를 던지지 않는 경우, 재정의된 자식 메서드도 체크 예외를 던질 수 없다.
   * 2) 부모가 체크 예외를 던지더라도 자식은 부모가 던진 체크 예외 및 하위 타입의 예외만 던질 수 있다.
   */

  // 실제 java에서 정의되어있는 Runnable 인터페이스의 메서드
  @FunctionalInterface
  public interface Runnable {
    /**
     * Runs this operation.
     */
    void run();
  }
}
