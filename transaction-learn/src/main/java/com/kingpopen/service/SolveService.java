package com.kingpopen.service;

import com.kingpopen.infrastructure.dao.StockMapper;
import com.kingpopen.model.Stock;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

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

  public void insertStock(final List<Stock> stocks) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
      // 这个地方获取的是DefaultSqlSession
      int cnt = 0;
      StockMapper stockMapper = sqlSession.getMapper(StockMapper.class);
      for (Stock item : stocks) {
        cnt += 1;
        stockMapper.save(item);
        if (cnt % 10 == 0) {
          sqlSession.commit();

          if (cnt == 20) {
            sqlSession.rollback();
          }
        }
      }
    } catch (Exception e) {
      log.error("插入数据出现异常!", e);
    }
  }
}