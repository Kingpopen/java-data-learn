package com.kingpopen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description jdbc的demo
 * @date 2024/4/20 19:36:57
 */
@Slf4j
public class JdbcCrud {

  public static final String URL = "jdbc:mysql://localhost:13306/mydatabase";
  public static final String USERNAME = "root";
  public static final String PASSWORD = "root";

  public void doCrud(final Consumer<Connection> foo) {
    // 加载jdbc的驱动 8.x 版本中已经不需要显示的加载驱动了
    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
      foo.accept(connection);
    } catch (Exception e) {
      log.error("数据库执行异常!", e);
      throw new RuntimeException("数据库执行异常！", e);
    }
  }

  // 插入
  public void insert(final Connection connection) {
    final String sql = "insert into user(username, identity_id, address) value('李四', '1002', '深圳市');";
    try {
      // 1. 获取statement
      Statement statement = connection.createStatement();
      // 2. 执行
      int cnt = statement.executeUpdate(sql);
      if (cnt != 0) {
        log.info("向数据库插入数据成功!");
      }
    } catch (Exception e) {
      log.error("数据插入异常！执行的sql语句为:{}", sql, e);
      throw new RuntimeException("数据库查询异常!", e);
    }
  }

  // 查询
  public void query(final Connection connection) {
    final String sql = "select * from user;";
    try {
      // 1. 获取statement
      Statement statement = connection.createStatement();
      // 2. 执行
      ResultSet res = statement.executeQuery(sql);
      // 3. 遍历结果
      while (res.next()) {
        int id = res.getInt("id");
        String username = res.getString("username");
        String identityId = res.getString("identity_id");
        String address = res.getString("address");
        log.info("{}\t{}\t{}\t{}", id, username, identityId, address);
      }
    } catch (Exception e) {
      log.error("数据查询异常！执行的sql语句为:{}", sql, e);
      throw new RuntimeException("数据库查询异常!", e);
    }
  }

  // 修改
  public void update(final Connection connection) {
    String sql = "update user set address = '硅谷' where username = '李四';";
    try {
      // 1. 获取statement
      Statement statement = connection.createStatement();
      // 2. 执行
      int cnt = statement.executeUpdate(sql);
      if (cnt != 0) {
        log.info("数据库更新成功！");
      }
    } catch (Exception e) {
      log.info("数据更新失败！执行的sql语句为:{}", sql, e);
      throw new RuntimeException("数据更新失败!", e);
    }
  }

  // 删除
  public void delete(final Connection connection) {
    String sql = "delete from user where username = '李四';";
    try {
      // 1. 获取statement
      Statement statement = connection.createStatement();
      // 2. 执行
      int cnt = statement.executeUpdate(sql);
      if (cnt != 0) {
        log.info("数据库删除成功！");
      }
    } catch (Exception e) {
      log.info("数据删除失败！执行的sql语句为:{}", sql, e);
      throw new RuntimeException("数据更新失败!", e);
    }
  }

}
