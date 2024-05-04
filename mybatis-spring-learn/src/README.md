## Mybatis-Spring学习
链接：https://mybatis.org/spring/zh_CN/index.html \
- [x] 基础的学习
- [ ] 关于事务的学习 \
主要分为几个步骤:
1. 创建数据源
2. 创建SqlSessionFactory, 依赖以下两样:
   * 数据源
   * mapper的xml路径
3. 实现Mapper接口

上面的Bean的创建和管理都是交给Spring
### 1. 创建数据源
在配置文件中使用@Bean注解生成对应的Bean
```java
@Bean
public DataSource dataSource(){
    String resource="db.properties";
    InputStream is=Resources.getResourceAsStream(resource))
    Properties properties=new Properties();
    properties.load(is);
    return DruidDataSourceFactory.createDataSource(properties);
}
```
### 2. 创建SqlSessionFactory
通过Spring中的FactoryBean自定义实例化bean的逻辑，意味着只需要创建\
SqlSessionFactoryBean：这是mybatis-spring中的类，实现了FactoryBean的接口 \

SqlSessionFactoryBean中可以指定mybatis-config.xml中的内容，\
也可以直接指定mybatis-config.xml的路径。\
它的的目的主要是构造出 SqlSessionFactory

```java
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
    Resource[] mapperResource = new PathMatchingResourcePatternResolver().getResources(
          "mybatis/mapper/*.xml");
    sqlSessionFactoryBean.setMapperLocations(mapperResource);
    return sqlSessionFactoryBean;
  }
```
### 3. 实现Mapper接口
mapper接口不需要手动实现，而是通过注册的方法，交给Spring通过动态代理的方式进行实现。 \
我们需要做的是指定mapper接口的位置信息\
我在配置类上添加@MapperScan注解指定了对应mapper接口的位置，
Spring就会创建对应的Mapper的Bean

```java
@Configuration
@MapperScan("com.kingpopen.infrastructure.dao")
public class MybatisConfig {
   ......
}
```

### SqlSessionTemplate
这是mybatis-spring中实现的SqlSession类，之前默认的实现是DefaultSqlSession \
在Spring中推荐使用SqlSessionTemplate(线程安全的) \
在Spring帮忙生成的Mapper类中，都是使用的SqlSessionTemplate进行的具体的操作。\
生成它的方式也比较简单，只需要传入SqlSessionFactory作为一个参数就好
```java
  @Bean
  public SqlSessionTemplate sqlSessionTemplate(
      @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }
```