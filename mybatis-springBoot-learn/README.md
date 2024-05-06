### mybatis-springBoot-learn
- [x] 入门
- [x] 批处理
- [ ] 事务
#### 1. 入门
搭建流程
1. springBoot的依赖(starter)
```xml
    <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>2.3.2</version>
    </dependency>
```
mybatis-spring-boot-starter 中引入了 mybatis-spring-boot-autoconfigure

通过这个包实现mybatis的自动配置。
```java
// 其中含有配置类 MybatisProperties.class, 
// 通过在application.properties 中配置 mybatis的信息就可以给这个配置类赋值，省去了mybatis-config.xml文件
@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
public class MybatisProperties {

  public static final String MYBATIS_PREFIX = "mybatis";

  private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

  private String configLocation;

  private String[] mapperLocations;

  private String typeAliasesPackage;

  private Class<?> typeAliasesSuperType;
  ......
}
```
```java
// MybatisAutoConfiguration 是真正的自动配置类
// @EnableConfigurationProperties(MybatisProperties.class) 使得MybatisProperties成为一个配置类
@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, MybatisLanguageDriverAutoConfiguration.class })
public class MybatisAutoConfiguration implements InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(MybatisAutoConfiguration.class);

  // 通过构造器注入的方式 注入该Bean
  private final MybatisProperties properties;

  private final Interceptor[] interceptors;
  ......
  
  // 生成 SqlSessionFactory的Bean
  @Bean
  @ConditionalOnMissingBean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
    factory.setDataSource(dataSource);
    ......
  // 生成 SqlSessionTemplate的Bean
  @Bean
  @ConditionalOnMissingBean 
  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
      ExecutorType executorType = this.properties.getExecutorType();
      if (executorType != null) {
        return new SqlSessionTemplate(sqlSessionFactory, executorType);
      } else {
        return new SqlSessionTemplate(sqlSessionFactory);
      }
    }
}
```
2. Datasource\
Datasource也是可以自动配置的, 这个自动配置是在springBoot-autoconfigure中提前定义好的。
只需要在application.yaml中设置相关属性就好了。
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:13306/mydatabase?serverTimezone=GMT%2B8&useSSL=false&rewriteBatchedStatements=true
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
    initial-size: 1
    min-idle: 1
    max-active: 5
    test-on-borrow: true
    validation-query: SELECT 1
```
3. 配置mybatis-config的信息\
mybatis-config的信息只需要在application.yaml中配置mapper的xml的地址就好了
```yaml
mybatis:
  mapper-locations: mybatis/mapper/*.xml
```
4. 配置mapper地址\
一般都是定义mapper接口，实现交给spring进行动态代理生成，为此需要指定mapper接口的路径。\
**注：生成的mapper动态代理类内部使用的SqlSession是SqlSessionTemplate**
* 方法1：@MapperScan
> 在Application.class上加上注解，并指定需要扫描的mapper接口路径
* 方法2：@Mapper
> 在mapper接口类上方直接加上@Mapper注解

SpringBoot会帮忙生成：
* SqlSessionFactory
* SqlSessionTemplate
* Mapper的代理实现类, 其中使用的是SqlSessionTemplate

5. 感悟\
SpringBoot 自动配置的通常步骤：
* 相关的属性的配置类：主要用于接收application.yaml中的配置内容，例如：
  * Mybatis中的MybatisProperties
  * Datasource中的DataSourceProperties
  * ......
* 自动配置类：帮忙生成相关的Bean，例如：
  * Mybatis中的MybatisAutoConfiguration类
  * Datasource中的DataSourceAutoConfiguration类
  * 通常使用：@EnableConfigurationProperties 使得相关的属性类成为一个配置类。
* 自动配置类通常依赖一些注解才会生效，例如：
  * @ConditionalOnClass
  * @ConditionalOnProperty

#### 2. 批处理
使用SqlSession的批处理模式 和 insert into values(),() ...模式进行对比实验
* 批处理模式
  * SqlSession开启：ExecutorType.BATCH
  * 连接串添加：rewriteBatchedStatements=true
  * 100w条数据大概耗时：8.725s
```java
// 需要注意的是，通过这种方式获取的SqlSession是DefaultSqlSession，不是SqlSessionTemplate
SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false));
UserMapper mapper = sqlSession.getMapper(UserMapper.class);
partitions.forEach(partition -> {
  partition.forEach(mapper::insert);
  sqlSession.commit();
});
```
* insert into values(),() ...
  * 设置一条语句插入的Batch size大小（实验设的10w）
  * 100w条数据大概耗时：13.212s

从结果来看使用批处理的方式还是效率高一些（不同电脑配置结果可能不一样，但趋势大概是一样的），设置rewriteBatchedStatements=true进行优化的步骤其实就是转化为insert into values(),(),...

但是如果直接使用insert into values(),(),... 需要自己设置一个Batch size的大小，设小了网络IO的次数就多了，设大了可能会出现单条SQL语句过大的异常

综合来看，不如直接使用批处理模式，它内部会帮忙计算一个合适的Batch size然后转化为insert into values(),()...的形式进行插入。