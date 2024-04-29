package com.kingpopen;

import com.kingpopen.jdbclearn.JdbcCrud;
import com.kingpopen.jdbclearn.JdbcWithTransactionUtil;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 测试
 * @date 2024/4/24 12:55:17
 */
@Slf4j
public class JdbcWithTransactionTest {

  // 测试一下jdbc的事务功能
  @Test
  public void testJdbcTransaction() {
    Connection connection = JdbcWithTransactionUtil.getConnection();
    try {
      JdbcCrud api = new JdbcCrud();
      // 1. 执行插入
      api.insertPrepare(connection);

      // 人为制造一个异常
      int i = 1 / 0;

      // 2. 执行删除
      api.deletePrepare(connection);
      // 3. 提交
      connection.commit();

      // 4. 进行查询
      api.queryPrepare(connection);
    } catch (Exception e) {
      try {
        // 回滚
        connection.rollback();
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
      log.error("执行操作出现异常，进行回滚！", e);
    } finally {
      // 5. 释放连接
      JdbcWithTransactionUtil.release();
    }
  }

  // 多个次请求是否会出现异常
  @Test
  public void testJdbcTransactionHighConcurrency(){
    JdbcCrud api = new JdbcCrud();
    Connection connection = JdbcWithTransactionUtil.getConnection();
    log.info("======> connection is:{}", connection);
    // 正常插入数据
    try{
      api.insertPrepare(connection);
      connection.commit();
    }catch (Exception e){
      throw new RuntimeException("出现异常！", e);
    }finally {
      JdbcWithTransactionUtil.release();
    }


    // 发生回滚的情况
    connection = JdbcWithTransactionUtil.getConnection();
    log.info("======> connection is:{}", connection);
    try{
      api.insertPrepare(connection);
      connection.rollback();
    }catch (Exception e){
      throw new RuntimeException("出现异常！", e);
    }

    // 进行查询
    api.queryPrepare(connection);
  }
}
