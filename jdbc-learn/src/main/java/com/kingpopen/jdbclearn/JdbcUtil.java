package com.kingpopen.jdbclearn;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description Jdbc工具类
 * @date 2024/04/22 23:02:20
 */
@Slf4j
public class JdbcUtil {
  private static final DataSource dataSource;
  // 将单个请求用到的连接，放在threadlocal中存储，方便一个线程在多次数据库操作，使用的都是同意一个连接
  private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

  // 使用静态代码块 初始化数据库连接池
  static {
    try {
      // 1. 创建配置文件
      Properties properties = new Properties();
      InputStream is = JdbcUtil.class.getClassLoader()
          .getResourceAsStream("db.properties");
      properties.load(is);
      // 2. 创建数据库连接池
      dataSource = DruidDataSourceFactory.createDataSource(properties);
    } catch (Exception e){
      log.error("创建数据库连接池失败!", e);
      throw new RuntimeException("创建数据库连接池失败!", e);
    }
  }

  /**
   * 获取数据库连接
   * @return Connection
   */
  public static Connection getConnection(){
    try {
      Connection connection = threadLocal.get();
      if (Objects.isNull(connection)){
        connection = dataSource.getConnection();
        threadLocal.set(connection);
      }
      return connection;
    }catch (Exception e){
      log.error("获取数据库连接失败!", e);
      throw new RuntimeException("获取数据库连接失败！", e);
    }
  }

  /**
   * 释放数据库连接
   */
  public static void release() {
    try {
      Connection connection = threadLocal.get();
      if (Objects.nonNull(connection)) {
        // 移除对应的connection
        threadLocal.remove();
        // 关闭connection
        connection.close();
      }
    } catch (SQLException e) {
      log.error("释放数据库连接失败!", e);
      throw new RuntimeException("释放数据库连接失败!", e);
    }
  }
}
