package com.kingpopen.mybatislearn;

import com.kingpopen.mybatislearn.dao.mapper.UserMapper;
import com.kingpopen.mybatislearn.infrastructure.common.SqlSessionUtil;
import com.kingpopen.mybatislearn.model.User;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description mybatis中参数的使用
 * @date 2024/5/2 21:41:31
 */
public class MybatisParam {

  public void queryByUsername(final String username) {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    List<User> users = mapper.findByName(username);
    sqlSession.close();
    users.forEach(System.out::println);
  }

  public void queryByLikeName(final String username) {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    List<User> users = mapper.findByLikeName(username);
    sqlSession.close();
    users.forEach(System.out::println);
  }


}
