package com.kingpopen.jdbclearn;

import static com.kingpopen.jdbclearn.common.Const.PASSWORD;
import static com.kingpopen.jdbclearn.common.Const.USERNAME;

import com.kingpopen.jdbclearn.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 批处理
 * @date 2024/4/21 00:31:41
 */
@Slf4j
public class JdbcBatch {

  /**
   * @param url   获取数据库连接的url
   * @param users user信息
   * @param foo   crud的操作函数
   */
  public void doCrud(final String url, final List<User> users,
      final BiConsumer<Connection, List<User>> foo) {
    //    Class.forName("com.mysql.cj.jdbc.Driver");   mysql8.x 已经可以不需要这行代码
    long begin = System.currentTimeMillis();
    try (Connection connection = DriverManager.getConnection(url, USERNAME, PASSWORD)) {
      foo.accept(connection, users);
    } catch (Exception e) {
      log.info("产生数据库连接出现异常！", e);
    }
    long end = System.currentTimeMillis();
    log.info("crud操作耗时:{}", end - begin);
  }

  // 普通的插入
  public void insertNormal(final Connection connection, final List<User> users) {
    // 1. 获取prepareStatement
    try {
      final String sql = "insert into user(username, identity_id, address) values(?, ?, ?)";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      for (User user : users) {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getIdentityId());
        preparedStatement.setString(3, user.getAddress());
        // 2. 进行执行
        preparedStatement.executeUpdate();
      }
    } catch (Exception e) {
      log.info("插入数据出现异常!", e);
    }
  }

  // 批量的插入
  public void insertBatch(final Connection connection, final List<User> users) {
    // 1. 获取prepareStatement
    try {
      final String sql = "insert into user(username, identity_id, address) values(?, ?, ?)";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      for (User user : users) {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getIdentityId());
        preparedStatement.setString(3, user.getAddress());
        // 2. 添加
        preparedStatement.addBatch();
      }
      // 3. 批量执行
      preparedStatement.executeBatch();
    } catch (Exception e) {
      log.info("插入数据出现异常!", e);
    }
  }

  // 含唯一键索引（username, address, identity_id）
  public void insertNormalWithUnique(final Connection connection, final List<User> users) {
    // 1. 获取prepareStatement
    try {
      final String sql = "insert ignore into user_tmp(username, identity_id, address) values(?, ?, ?)";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      for (User user : users) {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getIdentityId());
        preparedStatement.setString(3, user.getAddress());
        // 2. 进行执行
        preparedStatement.executeUpdate();
      }
    } catch (Exception e) {
      log.info("插入数据出现异常!", e);
    }
  }

  // 含有唯一键索引
  public void insertBatchWithUnique(final Connection connection, final List<User> users) {
    // 1. 获取prepareStatement
    try {
      final String sql = "insert ignore into user_tmp(username, identity_id, address) values(?, ?, ?)";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      for (User user : users) {
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getIdentityId());
        preparedStatement.setString(3, user.getAddress());
        // 2. 添加
        preparedStatement.addBatch();
      }
      // 3. 批量执行
      preparedStatement.executeBatch();
    } catch (Exception e) {
      log.info("插入数据出现异常!", e);
    }
  }
}
