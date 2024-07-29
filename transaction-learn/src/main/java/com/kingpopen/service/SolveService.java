package com.kingpopen.service;

import com.kingpopen.infrastructure.dao.StockMapper;
import com.kingpopen.model.Stock;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 解决bug的方案
 * @date 2024/7/7 15:18:47
 */
@Slf4j
@Service
public class SolveService {

  @Resource
  private SqlSessionFactory sqlSessionFactory;

  @Resource
  private SqlSessionTemplate sqlSessionTemplate;

  @Resource
  private PlatformTransactionManager transactionManager;

  @Resource
  private StockMapper stockMapper;

  public void insertStockWithRollback(final List<Stock> stocks) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      // 这个地方获取的是DefaultSqlSession
//      sqlSession.getConnection().setAutoCommit(false);
      int cnt = 0;
      StockMapper stockMapper = sqlSession.getMapper(StockMapper.class);
      for (Stock item : stocks) {
        cnt += 1;
        stockMapper.save(item);
        if (cnt % 10 == 0) {
          sqlSession.commit();
          if (cnt == 20) {
            sqlSession.rollback();
            break;
          }
        }
        sqlSession.commit();
      }
    } catch (Exception e) {
      log.error("插入数据出现异常!", e);
    }
  }

  public void insertWithNoRollback(final List<Stock> stocks) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      // 这个地方获取的是DefaultSqlSession
//      sqlSession.getConnection().setAutoCommit(false);
      int cnt = 0;
      StockMapper stockMapper = sqlSession.getMapper(StockMapper.class);
      for (Stock item : stocks) {
        cnt += 1;
        stockMapper.save(item);
        if (cnt % 10 == 0) {
          sqlSession.commit();
        }
      }
      sqlSession.commit();
    } catch (Exception e) {
      log.error("插入数据出现异常!", e);
    }
  }

  public void demo1(final List<Stock> stocks) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      // 这个地方获取的是DefaultSqlSession
//      sqlSession.getConnection().setAutoCommit(false);
      int cnt = 0;
      StockMapper stockMapper = sqlSession.getMapper(StockMapper.class);
      TransactionStatus txStatus = transactionManager.getTransaction(
          new DefaultTransactionDefinition());
      for (Stock item : stocks) {
        cnt += 1;
        stockMapper.save(item);
        if (cnt % 10 == 0) {
          transactionManager.commit(txStatus);
        }
      }
      txStatus = transactionManager.getTransaction(
          new DefaultTransactionDefinition());
      transactionManager.commit(txStatus);
    } catch (Exception e) {
      log.error("插入数据出现异常!", e);
    }
  }

  public void tt1(final List<Stock> stocks) {
    TransactionStatus txStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition());
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      StockMapper stockMapper = sqlSessionTemplate.getMapper(StockMapper.class);
      for (Stock item : stocks) {
        stockMapper.save(item);
      }
      transactionManager.commit(txStatus);
    } catch (Exception e) {
      transactionManager.rollback(txStatus);
    }
  }

  public void tt2(final List<Stock> stocks) {
    TransactionStatus txStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition());
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      StockMapper stockMapper = sqlSessionTemplate.getMapper(StockMapper.class);
      for (Stock item : stocks) {
        stockMapper.save(item);
      }
      int x = 1 / 0;
      transactionManager.commit(txStatus);
    } catch (Exception e) {
      transactionManager.rollback(txStatus);
    }
  }


  public void demo2(final List<Stock> stocks) {
    TransactionStatus txStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition());
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      // 这个地方获取的是DefaultSqlSession
      sqlSession.getConnection().setAutoCommit(false);
      int cnt = 0;
      StockMapper stockMapper = sqlSession.getMapper(StockMapper.class);
      for (Stock item : stocks) {
        cnt += 1;
        stockMapper.save(item);
        if (cnt % 10 == 0) {
          transactionManager.commit(txStatus);
        }

        if (cnt % 20 == 0) {
          transactionManager.rollback(txStatus);
          break;
        }
      }
      transactionManager.commit(txStatus);
    } catch (Exception e) {
      log.error("插入数据出现异常!", e);
    }
  }


  public void demo3(final List<Stock> stocks) {
    System.out.println("size is:" + stocks.size());
    try {
      int cnt = 0;
      StockMapper stockMapper = sqlSessionTemplate.getMapper(StockMapper.class);
      for (Stock item : stocks) {
        cnt += 1;
        stockMapper.save(item);
        if (cnt % 10 == 0) {
          TransactionStatus txStatus = transactionManager.getTransaction(
              new DefaultTransactionDefinition());
          transactionManager.commit(txStatus);
        }
      }
      TransactionStatus txStatus = transactionManager.getTransaction(
          new DefaultTransactionDefinition());
      transactionManager.commit(txStatus);
    } catch (Exception e) {
      log.error("出现异常!", e);
    }
  }

  public void demo4(final List<Stock> stocks) {
    TransactionStatus txStatus = transactionManager.getTransaction(
        new DefaultTransactionDefinition());
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      // 这个地方获取的是DefaultSqlSession
      sqlSession.getConnection().setAutoCommit(false);
      int cnt = 0;
      StockMapper stockMapper = sqlSession.getMapper(StockMapper.class);
      for (Stock item : stocks) {
        cnt += 1;
        stockMapper.save(item);
        if (cnt % 10 == 0) {
          transactionManager.commit(txStatus);
        }

        if (cnt % 20 == 0) {
          transactionManager.rollback(txStatus);
          break;
        }
      }
      transactionManager.commit(txStatus);
    } catch (Exception e) {
      log.error("插入数据出现异常!", e);
    }
  }

  /**
   * 最佳实践
   */
  public void bestPractice(final List<Stock> stocks){
    StockMapper mapper = sqlSessionTemplate.getMapper(StockMapper.class);
    for (Stock stock : stocks){
      TransactionStatus txStatus = transactionManager.getTransaction(
          new DefaultTransactionDefinition());
      stocks.forEach(mapper::save);
      try{
        transactionManager.commit(txStatus);
      }catch (Exception e){
        transactionManager.rollback(txStatus);
      }
    }

  }


  public void rollBackDemo(final List<Stock> stocks) {
    CompletableFuture<Void> future =
        CompletableFuture.runAsync(() -> insertWithNoRollback(stocks))
            .thenRunAsync(() -> insertStockWithRollback(stocks));
    try {
      future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException("出现中断 或 执行异常!", e);
    } catch (Exception e) {
      throw new RuntimeException("出现异常!", e);
    }
  }

  public void rollBackDemo2(final List<Stock> stocks) {
    CompletableFuture<Void> future =
        CompletableFuture.runAsync(() -> tt1(stocks))
            .thenRunAsync(() -> tt2(stocks));
    try {
      future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException("出现中断 或 执行异常!", e);
    } catch (Exception e) {
      throw new RuntimeException("出现异常!", e);
    }
  }
}