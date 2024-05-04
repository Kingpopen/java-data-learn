package com.kingpopen.mybatislearn;

import com.kingpopen.mybatislearn.dao.mapper.DynamicSqlUserMapper;
import com.kingpopen.mybatislearn.infrastructure.common.SqlSessionUtil;
import com.kingpopen.mybatislearn.model.User;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 动态sql
 * @date 2024/5/3 10:19:12
 */
public class MybatisDynamicSQL {

  public void queryByIf(final User user) {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
    DynamicSqlUserMapper mapper = sqlSession.getMapper(DynamicSqlUserMapper.class);
    List<User> users = mapper.findByIf(user);
    users.forEach(System.out::println);

    sqlSession.close();
  }

  public void queryByWhere(final User user) {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
    DynamicSqlUserMapper mapper = sqlSession.getMapper(DynamicSqlUserMapper.class);
    List<User> users = mapper.findByWhere(user);
    users.forEach(System.out::println);

    sqlSession.close();
  }

  public void queryByTrim(final User user) {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
    DynamicSqlUserMapper mapper = sqlSession.getMapper(DynamicSqlUserMapper.class);
    List<User> users = mapper.findByTrim(user);
    users.forEach(System.out::println);

    sqlSession.close();
  }

  public void queryByChoose(final User user) {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
    DynamicSqlUserMapper mapper = sqlSession.getMapper(DynamicSqlUserMapper.class);
    List<User> users = mapper.findByChoose(user);
    users.forEach(System.out::println);

    sqlSession.close();
  }

  public void queryByForeach(final Integer[] identityIds) {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
    DynamicSqlUserMapper mapper = sqlSession.getMapper(DynamicSqlUserMapper.class);
    List<User> users = mapper.findByForeach(identityIds);
    users.forEach(System.out::println);

    sqlSession.close();
  }

  public void queryBySql(final Integer pageSize) {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
    DynamicSqlUserMapper mapper = sqlSession.getMapper(DynamicSqlUserMapper.class);
    List<User> users = mapper.findBySql(pageSize);
    users.forEach(System.out::println);

    sqlSession.close();
  }
}
