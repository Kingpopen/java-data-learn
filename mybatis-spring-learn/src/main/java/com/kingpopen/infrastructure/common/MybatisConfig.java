package com.kingpopen.infrastructure.common;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.io.Resources;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description mybatis的相关配置
 * @date 2024/5/4 16:24:58
 */
@Configuration
@MapperScan("com.kingpopen.infrastructure.dao")
public class MybatisConfig {

  /**
   * 生成连接池的Bean
   *
   * @return DataSource
   */
  @Bean
  public DataSource dataSource() {
    String resource = "db.properties";
    try (InputStream is = Resources.getResourceAsStream(resource)) {
      Properties properties = new Properties();
      properties.load(is);
      return DruidDataSourceFactory.createDataSource(properties);
    } catch (IOException e) {
      throw new RuntimeException("读取连接池配置异常！", e);
    } catch (Exception e) {
      throw new RuntimeException("创建连接池出现异常！", e);
    }
  }

  /**
   * 生成SqlSessionFactory
   *
   * @param dataSource 连接池
   * @return
   */
  @Bean
  public SqlSessionFactoryBean sqlSessionFactory(
      @Qualifier("dataSource") DataSource dataSource) {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    // 1. 设置数据源
    sqlSessionFactoryBean.setDataSource(dataSource);

    // 2. 设置mybatis配置文件的地址
    ClassPathResource configResource = new ClassPathResource(
        "mybatis/mybatis-config.xml");
    sqlSessionFactoryBean.setConfigLocation(configResource);

    // 3. 设置mybatis的 mapper xml文件的地址
    try {
      Resource[] mapperResource = new PathMatchingResourcePatternResolver().getResources(
          "mybatis/mapper/*.xml");
      sqlSessionFactoryBean.setMapperLocations(mapperResource);
      return sqlSessionFactoryBean;
    } catch (IOException e) {
      throw new RuntimeException("解析mybatis的xml文件异常！", e);
    }
  }

  /**
   * 生成SqlSessionTemplate： 这是SqlSession的一种实现（线程安全），取代DefaultSqlSession
   * @param sqlSessionFactory
   * @return
   */
//  @Bean
//  public SqlSessionTemplate sqlSessionTemplate(
//      @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//    return new SqlSessionTemplate(sqlSessionFactory);
//  }
}
