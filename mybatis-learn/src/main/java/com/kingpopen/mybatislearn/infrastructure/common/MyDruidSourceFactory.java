package com.kingpopen.mybatislearn.infrastructure.common;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

/**
 * @author 彭锦波
 * @project java-data-learn
 * @description druid线程池 实现ibatis的接口
 * @date 2024/04/29 22:13:50
 */
public class MyDruidSourceFactory implements DataSourceFactory {

  private DataSource dataSource;

  @Override
  public void setProperties(Properties props) {
    try {
      dataSource = DruidDataSourceFactory.createDataSource(props);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public DataSource getDataSource() {
    return dataSource;
  }
}
