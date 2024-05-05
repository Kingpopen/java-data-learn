package com.kingpopen.mybatisspringboot;

import com.kingpopen.mybatisspringboot.infrastructure.dao.UserMapper;
import com.kingpopen.mybatisspringboot.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description mybatis的crud
 * @date 2024/5/5 13:29:04
 */
@Component
public class MybatisSpringBootCrud {

  @Autowired
  private UserMapper userMapper;

  public void queryByUsername(final String username) {
    List<User> users = userMapper.findByUsername(username);

    users.forEach(System.out::println);
  }

  public int insert(final User user) {
    return userMapper.insert(user);
  }

  public int update(final User user) {
    return userMapper.updateByUsername(user);
  }

  public int deleteByUsername(final String username) {
    return userMapper.deleteByUsername(username);
  }
}
