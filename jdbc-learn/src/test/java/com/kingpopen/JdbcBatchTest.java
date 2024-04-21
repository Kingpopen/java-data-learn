package com.kingpopen;

import static com.kingpopen.common.Const.URL_BATCH;
import static com.kingpopen.common.Const.URL_NORMAL;

import com.kingpopen.model.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description mysql 服务器中设置的 max_allowed_packet = 64MB
 * @date 2024/4/21 10:32:58
 */
class JdbcBatchTest {

  /**
   * 只有主键索引 数据量：10w 耗时：153524ms
   */
  @Test
  public void testNormalCrud() {
    // 1. 造数据
    List<User> users = new ArrayList<>();
    for (int i = 0; i < 100000; i++) {
      users.add(User.createUser());
    }
    // 2. 进行插入
    JdbcBatch api = new JdbcBatch();
    api.doCrud(URL_NORMAL, users, api::insertNormal);
  }

  /**
   * 只有主键索引 数据量：10w 耗时：1495ms
   * 只有主键索引 数据量：100w 耗时：16649ms
   */
  @Test
  public void testBatchCrud() {
    // 1. 造数据
    List<User> users = new ArrayList<>();
    for (int i = 0; i < 1000000; i++) {
      users.add(User.createUser());
    }
    // 2. 进行插入
    JdbcBatch api = new JdbcBatch();
    api.doCrud(URL_BATCH, users, api::insertBatch);
  }

  /**
   * 含有insert ignore, 并且有唯一键索引(username, address, identity_id) 数据量：10W  耗时：161271 ms
   * 不含ignore, 并且有唯一键索引(username, address, identity_id) 数据量：10W  耗时： ms
   */
  @Test
  public void testNormalCrudWithUnique() {
    // 1. 造数据
    List<User> users = new ArrayList<>();
    for (int i = 0; i < 100000; i++) {
      users.add(User.createUser());
    }
    // 2. 进行插入
    JdbcBatch api = new JdbcBatch();
    api.doCrud(URL_NORMAL, users, api::insertNormalWithUnique);
  }

  /**
   * 含有insert ignore, 并且有唯一键索引(username, address, identity_id) 数据量: 10W 耗时: 2367 ms
   * 含有insert ignore, 并且有唯一键索引(username, address, identity_id) 数据量: 100W 耗时: 26899ms
   * 不含ignore, 并且有唯一键索引(username, address, identity_id) 数据量: 100W 耗时: 30151ms
   */
  @Test
  public void testBatchCrudWithUnique() {
    // 1. 造数据
    List<User> users = new ArrayList<>();
    for (int i = 0; i < 1000000; i++) {
      users.add(User.createUser());
    }
    // 2. 进行插入
    JdbcBatch api = new JdbcBatch();
    api.doCrud(URL_BATCH, users, api::insertBatchWithUnique);
  }
}