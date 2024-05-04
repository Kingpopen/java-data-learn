package com.kingpopen.mybatislearn.dao.mapper;

import com.kingpopen.mybatislearn.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 用户mapper
 * @date 2024/04/24 21:15:06
 */
public interface UserMapper {
  int insert(@Param("user") final User user);

  int update(@Param("user") final User user);

  int deleteByName(final String username);

  List<User> findByName(@Param("username") final String username);

  List<User> findByLikeName(@Param("username") final String username);
}
