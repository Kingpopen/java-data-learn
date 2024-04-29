package com.kingpopen.mybatislearn.infrastructure.common;

import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description SqlSession 的工具类
 * @date 2024/04/29 22:46:10
 */
public class SqlSessionUtil {

  private static final SqlSessionFactory sqlSessionFactory;

  static {
    // 创建sqlSessionFactory
    String resource = "mybatis/mybatis-config.xml";
    try (InputStream is = Resources.getResourceAsStream(resource)) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    } catch (Exception e) {
      throw new RuntimeException("获取mybatis-config.xml的资源出现异常！", e);
    }
  }

  public static SqlSession getSqlSession(boolean autoCommit){
    return sqlSessionFactory.openSession(autoCommit);
  }

  public static SqlSession getSqlSession(){
    return sqlSessionFactory.openSession(false);
  }
}
