package com.kingpopen.mybatislearn;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.kingpopen.mybatislearn.dao.mapper.UserMapper;
import com.kingpopen.mybatislearn.model.User;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description mybatis入门 增删改查
 * @date 2024/04/24 21:08:17
 */
public class MybatisCrud {

  private static final Logger log = LogManager.getLogger(MybatisCrud.class);

  // 不使用xml构建 todo 这个会报错，还未解决
  public SqlSessionFactory createSqlSessionFactoryNoXml() {
    try {
      // 1. 构建DataSource对象
      Properties properties = new Properties();
      InputStream is = MybatisCrud.class.getClassLoader()
          .getResourceAsStream("db.properties");
      properties.load(is);
      DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

      // 2. 构建TransactionManager对象
      JdbcTransactionFactory transactionFactory = new JdbcTransactionFactory();

      // 3. 构建Environment类
      Environment environment = new Environment("development", transactionFactory, dataSource);
      Configuration configuration = new Configuration(environment);

      // 4. mapper
      configuration.addMapper(UserMapper.class);

      // 5. 构建SqlSessionFactory
      return new SqlSessionFactoryBuilder().build(configuration);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // 增加
  public void queryByName(final SqlSession session, final String username) {
    UserMapper mapper = session.getMapper(UserMapper.class);
    User user = mapper.findByName(username)
        .stream()
        .findFirst()
        .orElse(null);
    log.info("user is:{}", user);
  }

  // 删除
  public void save(final SqlSession session, final User user) {
    UserMapper mapper = session.getMapper(UserMapper.class);
    int cnt = mapper.insert(user);
    log.info("save cnt is:{}", cnt);
  }

  // 修改
  public void update(final SqlSession session, final User user) {
    UserMapper mapper = session.getMapper(UserMapper.class);
    int cnt = mapper.update(user);
    log.info("update cnt is:{}", cnt);
  }

  // 删除
  public void removeByName(final SqlSession session, final String username) {
    UserMapper mapper = session.getMapper(UserMapper.class);
    int cnt = mapper.deleteByName(username);
    log.info("delete cnt is:{}", cnt);
  }
}
