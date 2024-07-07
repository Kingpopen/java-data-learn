package com.kingpopen.service;

import com.kingpopen.model.Stock;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description
 * @date 2024/7/7 15:31:54
 */
@SpringBootTest
class SolveServiceTest {

  @Resource
  private SolveService service;
  @Test
  public void testInsert() {
    List<Stock> stocks = new ArrayList<>();
    int total = 10;
    int cnt = 0;
    for (int i = 0; i<total; i++){
      Stock stock1 = new Stock(null, String.valueOf(cnt + 1), 100, "深圳仓");
      Stock stock2 = new Stock(null, String.valueOf(cnt + 2), 150, "上海仓");
      cnt += 2;
      stocks.add(stock1);
      stocks.add(stock2);
    }
    service.insertStock(stocks);
  }
}