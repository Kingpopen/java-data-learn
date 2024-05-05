package com.kingpopen.mybatisspringboot.infrastructure.dao;

import com.kingpopen.mybatisspringboot.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 用户的Mapper
 * @date 2024/5/5 13:42:41
 */
public interface UserMapper {

  List<User> findByUsername(@Param("username") final String username);

  int insert(@Param("user") final User user);

  int updateByUsername(@Param("user") final User user);

  int deleteByUsername(@Param("username") final String username);

  int insertBatch(@Param("users") final List<User> users);
}
