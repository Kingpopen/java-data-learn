package com.kingpopen.infrastructure.common;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description Mybatis配置类
 * @date 2024/07/22 16:48:07
 */
@Configuration
public class MybatisConfig {

  @Bean
  public SqlSessionTemplate mySqlSessionTemplate(final SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
  }
}
