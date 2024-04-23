package com.kingpopen;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description jdbc 连接池
 * @date 2024/04/22 22:08:14
 */
@Slf4j
public class JdbcPool {

  public Connection getConnectionByDruid() {
    try {
      // 1. 读取配置文件
      Properties properties = new Properties();
      InputStream is = JdbcPool.class.getClassLoader()
          .getResourceAsStream("db.properties");
      properties.load(is);
      // 2. 创建连接池
      DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
      // 3. 获取连接
      return dataSource.getConnection();
    } catch (Exception e) {
      log.error("创建连接池失败！", e);
      throw new RuntimeException("创建连接池失败！", e);
    }
  }

  public void doCrudByDruid() {
    final String queryName = "张三";
    try (Connection connection = getConnectionByDruid()) {
      // 进行crud操作
      PreparedStatement preparedStatement = connection.prepareStatement(
          "select * from user where username = ?;");
      preparedStatement.setString(1, queryName);
      ResultSet res = preparedStatement.executeQuery();
      while (res.next()) {
        String username = res.getString("username");
        String identityId = res.getString("identity_id");
        String address = res.getString("address");
        log.info("{} \t {} \t {}", username, identityId, address);
      }
    } catch (Exception e) {
      log.error("执行查询操作失败！", e);
      throw new RuntimeException("执行查询失败！", e);
    }
  }
}
