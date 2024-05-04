package com.kingpopen;

import com.kingpopen.infrastructure.dao.UserMapper;
import com.kingpopen.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description crud
 * @date 2024/5/4 17:23:36
 */
@Component
public class MybatisSpringCrud {

  @Autowired
  private UserMapper userMapper;


  public void queryByUsername(final String username) {
    List<User> users = userMapper.findByUsername(username);

    users.forEach(System.out::println);
  }

  public int save(final User user) {
    return userMapper.insert(user);
  }

  public int updateByUsername(final User user) {
    return userMapper.updateByUsername(user);
  }

  public int deleteByUsername(final String username) {
    return userMapper.deleteByUsername(username);
  }
}
