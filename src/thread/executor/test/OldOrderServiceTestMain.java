package thread.executor.test;

public class OldOrderServiceTestMain {

  public static void main(String[] args) {
    String orderNo = "Order#1234";
    OldOrderService orderService = new OldOrderService();
    orderService.order(orderNo);
  }

  /** 총 실행시간: 3초
   * 22:17:30.253 [     main] 재고 업데이트: Order#1234
   * 22:17:31.260 [     main] 배송 시스템 알림: Order#1234
   * 22:17:32.262 [     main] 회계 시스템 업데이트: Order#1234
   * 22:17:33.266 [     main] 모든 주문 처리가 성공적으로 완료되었습니다.
   */

}
