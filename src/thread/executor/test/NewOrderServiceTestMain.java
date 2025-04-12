package thread.executor.test;

import static util.MyLogger.log;

import java.util.concurrent.ExecutionException;

public class NewOrderServiceTestMain {

  public static void main(String[] args) {
    String orderNo = "Order#1234";
    NewOrderService orderService = new NewOrderService();
    try {
      orderService.order(orderNo);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      log("주문 실패, 예외 발생: " + e.getMessage() + ", " + e.getCause());
      throw new RuntimeException(e);
    }
  }

  /** 총 실행시간: 1초
   * 22:23:58.778 [pool-1-thread-2] 배송 시스템 알림: Order#1234
   * 22:23:58.778 [pool-1-thread-1] 재고 업데이트: Order#1234
   * 22:23:58.778 [pool-1-thread-3] 회계 시스템 업데이트: Order#1234
   * 22:23:59.788 [     main] 모든 주문 처리가 성공적으로 완료되었습니다.
   */

}
