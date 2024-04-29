package com.kingpopen.jdbclearn;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Objects;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 含有事务操作的jdbc 工具类
 * @date 2024/4/24 12:21:38
 */
@Slf4j
public class JdbcWithTransactionUtil {

  private static final DataSource dataSource;
  private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

  // 使用static 块初始化
  static {
    try {
      // 构造properties
      Properties properties = new Properties();
      InputStream is = JdbcWithTransactionUtil.class.getClassLoader()
          .getResourceAsStream("db.properties");
      properties.load(is);
      dataSource = DruidDataSourceFactory.createDataSource(properties);
    } catch (Exception e) {
      log.error("构造连接池出现异常!");
      throw new RuntimeException("构造连接池出现异常！", e);
    }
  }

  // 获取连接
  public static Connection getConnection() {
    try {
      // 1. 获取连接
      Connection connection = threadLocal.get();
      if (Objects.isNull(connection)) {
        connection = dataSource.getConnection();
        threadLocal.set(connection);
      }
      // 2. 设置为手动提交事务
      connection.setAutoCommit(false);
      return connection;
    } catch (Exception e) {
      // TODO: 2024/4/24
      throw new RuntimeException("xxxx");
    }
  }

  // 释放连接
  public static void release() {
    try {
      Connection connection = threadLocal.get();
      if (Objects.nonNull(connection)) {
        threadLocal.remove();
        /*
         * 这个地方使用的是 DruidPooledConnection类，它一个代理类，这个地方代理的应该是JdbcConnection
         * 它重写了close方法，这意味着这个地方的connection.close() 并不意味着连接真的被关闭了，
         * 而是可能将它重新放到连接池中去了。
         */
        connection.setAutoCommit(true);
        connection.close();
      }
    } catch (Exception e) {
      // todo
      throw new RuntimeException("");
    }

  }
}
