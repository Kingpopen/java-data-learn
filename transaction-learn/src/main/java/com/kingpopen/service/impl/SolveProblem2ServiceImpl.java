package com.kingpopen.service.impl;

import com.kingpopen.infrastructure.dao.StockMapper;
import com.kingpopen.model.Stock;
import com.kingpopen.service.ProblemService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 解决问题
 * @date 2024/07/22 21:09:39
 */
@Slf4j
@Service("solveProblem2Service")
public class SolveProblem2ServiceImpl implements ProblemService {

  @Resource
  private SqlSessionFactory sqlSessionFactory;
  @Resource
  private PlatformTransactionManager transactionManager;

  /**
   * TODO 这种方式明明使用了事务，但是没有成功插入数据，需要后续再仔细看一下
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

  /**
   * 插入数据，没有rollback
   *
   * @param stocks
   */
  private void insertWithNoRollback(final List<Stock> stocks) {
    TransactionStatus txStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition());
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      StockMapper mapper = sqlSession.getMapper(StockMapper.class);
      stocks.forEach(mapper::save);
      transactionManager.commit(txStatus);
    } catch (Exception e) {
      log.error("插入Stock数据出现异常!", e);
    }
  }

  /**
   * 插入数据 存在手动rollback
   *
   * @param stocks
   */
  private void insertWithRollback(final List<Stock> stocks) {
    TransactionStatus txStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition());
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      StockMapper mapper = sqlSession.getMapper(StockMapper.class);
      stocks.forEach(mapper::save);
//      int x = 1 / 0;
      transactionManager.commit(txStatus);
    } catch (Exception e) {
      transactionManager.rollback(txStatus);
      log.error("插入Stock数据出现异常!", e);
    }
  }
}
