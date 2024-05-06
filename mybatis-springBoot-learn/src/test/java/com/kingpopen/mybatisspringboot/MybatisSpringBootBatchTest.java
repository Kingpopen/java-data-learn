package com.kingpopen.mybatisspringboot;

import com.kingpopen.mybatisspringboot.model.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description
 * @date 2024/5/5 17:07:55
 */
@SpringBootTest
class MybatisSpringBootBatchTest {
  @Autowired
  private MybatisSpringBootBatch api;
  @Test
  public void testBatchByOneValue(){
    List<User> users = new ArrayList<>();
    for (int i = 0; i<1000000; i++){
      users.add(User.createUserByRandom());
    }
    api.batchInsertByOneValue(users);
  }

  @Test
  public void testBatchByMultiValue(){
    List<User> users = new ArrayList<>();
    for (int i = 0; i<1000000; i++){
      users.add(User.createUserByRandom());
    }
    api.batchInsertByMultiValue(users);
  }
}