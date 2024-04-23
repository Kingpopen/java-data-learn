package com.kingpopen;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import org.junit.jupiter.api.Test;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description JdbcUtil的测试类
 * @date 2024/4/23 12:56:29
 */
class JdbcUtilTest {

  @Test
  public void testJdbcUtil(){
    JdbcCrud api = new JdbcCrud();
    Connection connection = JdbcUtil.getConnection();
    // 执行 查询
    api.queryPrepare(connection);
    // 执行 插入
    api.insertPrepare(connection);
    // 执行修改
    api.updatePrepare(connection);
    // 执行查询
    api.queryPrepare(connection);
    // 执行删除
    api.deletePrepare(connection);
    // 释放连接
    JdbcUtil.release();
  }
}