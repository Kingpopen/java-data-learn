package com.kingpopen.mybatislearn;

import com.kingpopen.mybatislearn.MybatisCrud;
import com.kingpopen.mybatislearn.infrastructure.common.SqlSessionUtil;
import com.kingpopen.mybatislearn.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description
 * @date 2024/04/24 22:16:20
 */
@Slf4j
class MybatisCrudTest {

  @Test
  public void testMybatisCrudWithXml() {
    MybatisCrud api = new MybatisCrud();
    // 1. 创建SqlSession
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();

    // 2. 进行插入
    User user = User.createUser("黄老爷", 1000, "北京市");
    api.save(sqlSession, user);
    // 需要commit 后续才能查询到结果
    sqlSession.commit();

    //2. 进行查询
    api.queryByName(sqlSession, "黄老爷");
    sqlSession.commit();

    //3. 执行修改
    user.setAddress("中国");
    api.update(sqlSession, user);
    sqlSession.commit();

    // 4. 查询
    api.queryByName(sqlSession, "黄老爷");
    sqlSession.commit();

    // 5. 删除
    api.removeByName(sqlSession, "黄老爷");
    sqlSession.commit();

    // 6. 查询
    api.queryByName(sqlSession, "黄老爷");
    sqlSession.commit();

    // 关闭
    sqlSession.close();
  }
}