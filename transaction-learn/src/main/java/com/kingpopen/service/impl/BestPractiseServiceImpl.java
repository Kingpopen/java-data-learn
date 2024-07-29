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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 最佳实践
 * @date 2024/07/22 21:31:24
 */
@Slf4j
@Service("bestPractiseService")
public class BestPractiseServiceImpl implements ProblemService {

  @Qualifier("mySqlSessionTemplate")
  @Resource
  private SqlSessionTemplate sqlSessionTemplate;

  @Resource
  private PlatformTransactionManager transactionManager;

  /**
   * TODO 可以再验证一下， 当前结果是对的，并且批量插入
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
    TransactionStatus txStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition());
    StockMapper mapper = sqlSessionTemplate.getMapper(StockMapper.class);
    stocks.forEach(mapper::save);
    transactionManager.commit(txStatus);
  }

  private void insertWithRollback(final List<Stock> stocks) {
    TransactionStatus txStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition());
    try{
      StockMapper mapper = sqlSessionTemplate.getMapper(StockMapper.class);
      int cnt = 0;
      for (Stock stock : stocks){
        cnt += 1;
        mapper.save(stock);
        if (cnt % 10 == 0){
          int x = 1 / 0;
        }
      }
    }catch (Exception e){
      transactionManager.rollback(txStatus);
    }
  }
}
