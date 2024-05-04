package com.kingpopen.infrastructure.dao;

import com.kingpopen.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 用户mapper
 * @date 2024/5/4 17:14:37
 */
public interface UserMapper {

  List<User> findByUsername(@Param("username") String username);

  int insert(@Param("user") User user);

  int updateByUsername(@Param("user") User user);

  int deleteByUsername(@Param("username") String username);

}
