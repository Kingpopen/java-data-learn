package com.kingpopen.mybatisspringboot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kingpopen.mybatisspringboot.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description
 * @date 2024/5/5 14:31:42
 */
@SpringBootTest
class MybatisSpringBootCrudTest {

  @Autowired
  private MybatisSpringBootCrud api;

  @Test
  public void testCrud() {
    User user;
    int cnt = 0;
    // 1. 插入
    user = User.createUser(null, "张麻子", "666", "成都市");
    cnt = api.insert(user);
    assertEquals(1, cnt);
    // 2. 修改
    user.setAddress("重庆市");
    cnt = api.update(user);
    assertEquals(1, cnt);
    // 3. 查询
    api.queryByUsername("张麻子");
    // 4. 删除
    cnt = api.deleteByUsername("张麻子");
    assertEquals(1, cnt);
    // 5. 查询
    api.queryByUsername("张麻子");
  }
}