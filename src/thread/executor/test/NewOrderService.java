package thread.executor.test;

import static util.MyLogger.log;
import static util.ThreadUtils.sleep;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 하나의 주문이 발생하면 추가로 3가지 일이 발생 <br>
 * 1. 재고 업데이트  <br>
 * 2. 배송 시스템 노티  <br>
 * 3. 회계 시스템 업데이트  <br>
 * 모두 완료되어야 주문 성공
 */
public class NewOrderService {

  public void order(String orderNo) throws InterruptedException, ExecutionException {
    ExecutorService es = Executors.newFixedThreadPool(3);
    List<Callable<Boolean>> callableList =
        List.of(
          new InventoryWork(orderNo),
          new ShippingWork(orderNo),
          new AccountingWork(orderNo)
        );

    List<Future<Boolean>> futureList = es.invokeAll(callableList);
    for (Future<Boolean> future : futureList) {
      Boolean result = future.get();
      if (!result) {
        log("일부 작업이 실패했습니다.");
        es.shutdown();
        return;
      }
    }

    log("모든 주문 처리가 성공적으로 완료되었습니다.");
    es.shutdown();
  }

  static class InventoryWork implements Callable<Boolean> {

    private final String orderNo;

    public InventoryWork(String orderNo) {
      this.orderNo = orderNo;
    }

    @Override
    public Boolean call() {
      log("재고 업데이트: " + orderNo);
      sleep(1000);
      return true;
    }
  }

  static class ShippingWork implements Callable<Boolean> {

    private final String orderNo;

    public ShippingWork(String orderNo) {
      this.orderNo = orderNo;
    }

    @Override
    public Boolean call() {
      log("배송 시스템 알림: " + orderNo);
      sleep(1000);
      return true;
    }
  }

  static class AccountingWork implements Callable<Boolean> {

    private final String orderNo;

    public AccountingWork(String orderNo) {
      this.orderNo = orderNo;
    }

    @Override
    public Boolean call() {
      log("회계 시스템 업데이트: " + orderNo);
      sleep(1000);
      return true;
    }
  }

}
