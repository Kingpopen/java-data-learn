package com.kingpopen.service.impl;

import com.kingpopen.model.Stock;
import com.kingpopen.service.StockService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 声明式事务的测试类
 * @date 2024/7/7 12:33:34
 */
@Slf4j
@SpringBootTest
class StockServiceDeclarativeImplTest {

  @Autowired
  private StockService stockService;

  @Test
  public void testGetById() {
    int id = 1;
    final Stock stock = stockService.getById(id);
    log.info("stock is:{}", stock);
  }


  @Test
  public void testUpdateStock() {
    int id = 1;
    final Stock stock = stockService.getById(id);
    stock.changeCount(10);
    stockService.updateStock(stock);
  }

  /**
   * 测试当出现异常时，会自动进行回滚
   */
  @Test
  public void testUpdateStockWithTransactionAndRunTimeException() {
    int id = 1;
    final Stock stock = stockService.getById(id);
    stock.changeCount(100);
    stockService.updateStockWithTransactionAndRunTimeException(stock);
  }

  /**
   * rollbackFor 指定unCheck Exception，也会进行回滚
   */
  @Test
  public void testUpdateStockWithTransactionException() throws IOException {
    int id = 1;
    final Stock stock = stockService.getById(id);
    stock.changeCount(250);
    stockService.updateStockWithTransactionAndUnCheckException(stock);
  }
}