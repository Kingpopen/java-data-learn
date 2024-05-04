package com.kingpopen.mybatislearn.dao.mapper;

import com.kingpopen.mybatislearn.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 动态Sql的User mapper
 * @date 2024/5/3 10:20:42
 */
public interface DynamicSqlUserMapper {
  List<User> findByIf(@Param("user") final User user);

  List<User> findByWhere(@Param("user") final User user);

  List<User> findByTrim(@Param("user") final User user);

  List<User> findByChoose(@Param("user") final User user);

  List<User> findByForeach(@Param("identityIds") Integer[] identityIds);
  List<User> findBySql(@Param("pageSize") final Integer pageSize);
}
