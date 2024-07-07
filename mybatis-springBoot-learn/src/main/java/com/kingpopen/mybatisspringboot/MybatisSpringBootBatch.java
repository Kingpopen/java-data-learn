package com.kingpopen.mybatisspringboot;

import com.google.common.collect.Lists;
import com.kingpopen.mybatisspringboot.infrastructure.dao.UserMapper;
import com.kingpopen.mybatisspringboot.model.User;
import java.util.List;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description 批量操作
 * @date 2024/5/5 15:58:04
 */
@Component
public class MybatisSpringBootBatch {

  private static final Logger log = LogManager.getLogger(MybatisSpringBootBatch.class);

  @Autowired
  private SqlSessionFactory sqlSessionFactory;

  /**
   * insert的内容每一次是一条数据
   * <p>
   *  1. 采用 ExecutorType.BATCH 模式进行插入
   * <p>
   * 2. mysql连接串中添加 rewriteBatchedStatements=true
   * <p>
   * 100W条数据 耗时：8725ms
   *
   * @param users: 插入的用户信息
   */
  public void batchInsertByOneValue(final List<User> users) {
    // 数据进行划分
    List<List<User>> partitions = Lists.partition(users, 100000);

    long begin = System.currentTimeMillis();
    try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
      UserMapper mapper = sqlSession.getMapper(UserMapper.class);
      partitions.forEach(partition -> {
        partition.forEach(mapper::insert);
        sqlSession.commit();
      });
    } catch (Exception e) {
      log.info("批量插入出现异常!", e);
      throw new RuntimeException("批量插入出现异常!", e);
    }
    long end = System.currentTimeMillis();
    log.info("耗时为:{}", end - begin);
  }

  /**
   * insert的内容是多条数据， 100w条数据，耗时：13212 ms
   *
   * @param users： 插入的用户信息
   */
  public void batchInsertByMultiValue(final List<User> users) {
    // 数据进行划分
    List<List<User>> partitions = Lists.partition(users, 100000);

    long begin = System.currentTimeMillis();
    try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
      UserMapper mapper = sqlSession.getMapper(UserMapper.class);
      partitions.forEach(partition -> {
        mapper.insertBatch(partition);
      });
      sqlSession.commit();
    } catch (Exception e) {
      log.info("批量插入出现异常!", e);
      throw new RuntimeException("批量插入出现异常!", e);
    }
    long end = System.currentTimeMillis();
    log.info("耗时为:{}", end - begin);
  }
}
