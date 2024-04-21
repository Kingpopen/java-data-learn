package com.kingpopen;

import static com.kingpopen.common.Const.PASSWORD;
import static com.kingpopen.common.Const.URL_NORMAL;
import static com.kingpopen.common.Const.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

  public void doCrud(final Consumer<Connection> foo) {
    // 加载jdbc的驱动 8.x 版本中已经不需要显示的加载驱动了
    try (Connection connection = DriverManager.getConnection(URL_NORMAL, USERNAME, PASSWORD)) {
      foo.accept(connection);
    } catch (Exception e) {
      log.error("数据库执行异常!", e);
      throw new RuntimeException("数据库执行异常！", e);
    }
  }

  // 插入
  public void insert(final Connection connection) {
    final String sql = "insert into user(username, identity_id, address) value('李四', '1002', '深圳市');";
    // 1. 获取statement
    try (Statement statement = connection.createStatement();) {
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
    // 1. 获取statement
    // 2. 执行
    try (Statement statement = connection.createStatement();
        ResultSet res = statement.executeQuery(sql);) {

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
    final String sql = "update user set address = '硅谷' where username = '李四';";
    // 1. 获取statement
    try (Statement statement = connection.createStatement();) {
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
    final String sql = "delete from user where username = '李四';";
    // 1. 获取statement
    try (Statement statement = connection.createStatement()) {
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

  // 插入 使用PrepareStatement
  public void insertPrepare(final Connection connection) {
    final String sql = "insert into user(username, identity_id, address) values (?, ?, ?)";
    // 1. 获取statement
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      // 2. 执行
      preparedStatement.setString(1, "李四");
      preparedStatement.setString(2, "1003");
      preparedStatement.setString(3, "广州市");
      int cnt = preparedStatement.executeUpdate();
      if (cnt != 0) {
        log.info("向数据库插入数据成功!");
      }
    } catch (Exception e) {
      log.error("数据插入异常！执行的sql语句为:{}", sql, e);
      throw new RuntimeException("数据插入异常！", e);
    }
  }

  public void queryPrepare(final Connection connection) {
    final String sql = "select * from user;";
    // 1. 获取statement
    // 2. 执行
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet res = preparedStatement.executeQuery(sql);) {
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

  public void updatePrepare(final Connection connection) {
    final String sql = "update user set address = ? where username = ?;";
    // 1. 获取statement
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      // 2. 执行
      preparedStatement.setString(1, "硅谷");
      preparedStatement.setString(2, "李四");
      int cnt = preparedStatement.executeUpdate();
      if (cnt != 0) {
        log.info("数据库更新成功！");
      }
    } catch (Exception e) {
      log.info("数据更新失败！执行的sql语句为:{}", sql, e);
      throw new RuntimeException("数据更新失败!", e);
    }
  }

  public void deletePrepare(final Connection connection) {
    final String sql = "delete from user where username = ?;";
    // 1. 获取statement
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
      // 2. 执行
      preparedStatement.setString(1, "李四");
      int cnt = preparedStatement.executeUpdate();
      if (cnt != 0) {
        log.info("数据库删除成功！");
      }
    } catch (Exception e) {
      log.info("数据删除失败！执行的sql语句为:{}", sql, e);
      throw new RuntimeException("数据更新失败!", e);
    }
  }


}
