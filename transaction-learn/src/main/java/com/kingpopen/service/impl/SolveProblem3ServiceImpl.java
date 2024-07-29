package com.kingpopen.service.impl;

import com.kingpopen.infrastructure.dao.StockMapper;
import com.kingpopen.model.Stock;
import com.kingpopen.service.ProblemService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 解决问题
 * @date 2024/07/22 21:19:28
 */
@Slf4j
@Service("solveProblem3Service")
public class SolveProblem3ServiceImpl implements ProblemService {

  @Resource
  private SqlSessionTemplate sqlSessionTemplate;

  /**
   * TODO 虽然这个能够正常的实现插入，并且也可以正常回退，但是插入结果不对，并且并不是批量插入。
   * @param stocks
   */
  @Override
  public void insertStocks(List<Stock> stocks) {
    CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      insertWithRollback(stocks);
    });

    CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      insertWithNoRollback(stocks);
    });

    try {
      future1.get();
      future2.get();
    } catch (InterruptedException | ExecutionException e) {
      log.error("出现异常!", e);
    }
  }

  private void insertWithNoRollback(final List<Stock> stocks) {
    StockMapper mapper = sqlSessionTemplate.getMapper(StockMapper.class);
    stocks.forEach(mapper::save);
  }

  private void insertWithRollback(final List<Stock> stocks) {
    StockMapper mapper = sqlSessionTemplate.getMapper(StockMapper.class);
    int cnt = 0;
    for (Stock stock : stocks){
      cnt += 1;
      mapper.save(stock);
      if (cnt % 10 == 0){
        int x = 1 / 0;
      }
    }
  }
}
