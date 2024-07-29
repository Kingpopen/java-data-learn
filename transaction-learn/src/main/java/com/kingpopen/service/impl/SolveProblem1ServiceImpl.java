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

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 解决问题
 * @date 2024/07/22 20:59:57
 */
@Slf4j
@Service("solveProblem1Service")
public class SolveProblem1ServiceImpl implements ProblemService {

  @Resource
  private SqlSessionFactory sqlSessionFactory;


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

    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
//      sqlSession.getConnection().setAutoCommit(false);
      StockMapper mapper = sqlSession.getMapper(StockMapper.class);
      int cnt = 0;
      for (Stock stock : stocks) {
        cnt += 1;
        mapper.save(stock);
        if (cnt % 10 == 0) {
          sqlSession.commit();
        }
      }
      if (cnt % 10 != 0) {
        sqlSession.commit();
      }
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
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
//      sqlSession.getConnection().setAutoCommit(false);
      StockMapper mapper = sqlSession.getMapper(StockMapper.class);
      int cnt = 0;
      for (Stock stock : stocks) {
        cnt += 1;
        mapper.save(stock);
        if (cnt % 10 == 0) {
          sqlSession.commit();
        }
      }
      // 手动rollback
      sqlSession.rollback();
    } catch (Exception e) {
      log.error("插入Stock数据出现异常!", e);
    }
  }
}
