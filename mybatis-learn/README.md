## Mybatis学习
### 1. Mybatis入门
#### 1. 构建SqlSessionFactory
SqlSessionFactory是一个接口，它的默认实现是:DefaultSqlSessionFactory
创建有两种方式：
1. 使用Mybatis-config.xml进行构建
> 其中含有 Datasource 和 TransactionManager的信息
> 
> Datasource 如果需要自定义，可以实现ibatis中的接口 DataSourceFactory
在构建时依赖于SqlSessionFactoryBuilder 解析配置文件 进行构建。  
> TransactionManager表示事务管理器，有两个两种选项：  
> 一种是：JDBC  
> 另一种是：Managed （交给三方容器进行事务管理）
2. 使用Java代码直接构建（当前我构建的方式会报错）
#### 2. 获取SqlSession
通过 SqlSessionFactory的openSession方法，可以指定是否自动提交
* mybatis中的sqlSession默认需要手动commit（autoCommit 默认为false）
* jdbc中的connection默认是自动提交

#### 3. 参数使用
* @Param注解可以指定作为参数的名称
```java
// 在mybatis的mapper的xml文件中就可以userId 获取到参数id的值
List<User> selectById(@Param("userId") Integer id);
```
* mybatis中的like查询尽量使用concat函数
**#{}会自动在变量外面加上''**
```roomsql
select username from user where username like concat('%', #{username}, '%')
```
* resultType中的类型很多都是mybatis预设的别名
> Integer 类型的别名有 Integer, integer \
> Map类型的别名是 map \
> ...
* order by中使用${}表示字段 不要使用 #{}(它会在对应的值外自动加上单引号)
```java
// 假如传入的column = "age"
List<Integer> findOrderByColumn(@Param("column") String column);
```
```roomsql
select age from user order by ${column}
-- 如果是#{column} 会被 解析为：
-- select age from user order by 'age';
```
#### 4. 一对多 和 多对一关系
1. 多对一的关系 （一个实体中包含有另一个实体作为属性）
> * 及联获取属性
> * resultMap + association标签 设置属性
> * 分步查询获取 （延迟加载）
2. 一对多的关系
> * collection 标签
> * 分步查询获取

#### 5. 动态SQL
主要是解决**sql语句拼接**的一些问题。
* if标签
> 能够通过判断字段值是否存在，从而决定是否添加某条sql语句,但是放在where中会有些问题。
> 例如：
```mybatisognl
select * 
from user 
where
<if test="user.username != null and user.username != ''">
  username = #{user.username}
</if>
<if test="user.age != null">
  and age = #{user.age}
</if>
```
> 如果username为空，age不为null，在where条件中就会多出来一个and
* where标签
> 会自动添加where关键字，并自动处理内容前的多余and/or 这一类的关键字。（内容后多余的and / or是不能去除的）
* trim标签
> prefix/suffix: 给内容前/后 添加内容\
> prefixOverrides/suffixOverrides: 给内容前/后 删除内容
> 
* choose, when, otherwise 标签 
> 相当于 java中的 if ... else if ... else
* foreach 标签
> collection(集合), item(集合中的元素)\
> separator(分隔符) \
> open(开始时添加的字符), close(结束时添加的字符)
* sql标签
> `<sql>标签` 用于sql片段的定义\
> `<include>标签` 用于sql片段的引用


### 源码学习
#### 1. SqlSession
Java对数据的底层操作都是基于JDBC相关API进行操作的，Mybatis为了封装JDBC的操作，
构建了一个顶级接口`SqlSession`，用于封装对数据库的操作:
```java
public interface SqlSession{
  // 列举几个比较重要的方法
  void rollback();
  void commit();
  Connection getConnection();
}
```
它有两个实现类：
* DefaultSqlSession (默认实现，非线程安全)
* ManageSqlSession （一种线程安全的实现）\
- [ ] 后续讨论一下 DefaultSqlSession 为什么不是线程安全的？

DefaultSqlSession 的代码中不是直接操作JDBC的API，而是将相关操作再次交给了一个Executor的类操作

```java
public class DefaultSqlSession implements SqlSession {

  private final Configuration configuration;
  private final Executor executor;
  
  // .......
  
  @Override
  public Connection getConnection() {
    try {
      return executor.getTransaction().getConnection();
    } catch (SQLException e) {
      throw ExceptionFactory.wrapException("Error getting a new connection.  Cause: " + e, e);
    }
  }
}
```
#### 2. Executor 
Executor是一个接口，其中定义操作数据库的方法:
```java
public interface Executor{
  // 列举几个常见方法
  void commit(boolean required) throws SQLException;
  void rollback(boolean required) throws SQLException;
}
```
它的实现类有好几个：
* BaseExecutor （抽象类）
* SimpleExecutor (非批量操作)
* BatchExecutor (批量操作)
* ReuseExecutor
* CachingExecutor

```java
public abstract class BaseExecutor{
  // 为了实现事务的管理，其中含有Transaction属性
  private Transaction transaction;
  // ......
  @Override
  public void commit(boolean required) throws SQLException {
    if (closed) {
      throw new ExecutorException("Cannot commit, transaction is already closed");
    }
    clearLocalCache();
    flushStatements();
    if (required) {
      transaction.commit();
    }
  }
}
```
#### 3. Transaction
Transaction 是一个事务操作的接口
```java
public interface Transaction{
  void commit();
  void rollback();
  Connection getConnection();
  // ......
}
```
Mybatis中对事务的具体操作其实都是依赖于这个接口的实现类：
1. JDBCTransaction
2. ManagedTransaction
3. SpringManagedTransaction (Spring-mybatis 提供)

JDBCTransaction中就含有对JDBC的API的常见操作: \
从代码中可以看出，JDBCTransaction实现事务主要就是通过connection的autoCommit = false来实现的,
当autoCommit = false的时候才需要手动commit。
```java
public class JDBCTransaction{
  
  private Connection connection;
  private Datasource datasource;
  
  // .......
  public void commit() throws SQLException {
    if (connection != null && !connection.getAutoCommit()) {
      if (log.isDebugEnabled()) {
        log.debug("Committing JDBC Connection [" + connection + "]");
      }
      connection.commit();
    }
  }
}
```
ManagedTransaction表示事务交给容器进行处理，
具体的容器最终都会提供一个响应的Transaction实现类，因此ManagedTransaction内部的
commit 和 rollback都是没有具体的实现内容的，因为它最终会被具体的容器的Transaction类所替换掉
```java
public class ManagedTransaction{
  @Override
  public void commit() throws SQLException {
    // Does nothing
  }

  @Override
  public void rollback() throws SQLException {
    // Does nothing
  }
}
```
例如Spring提供了一个SpringManagedTransaction实现类
```java
public class SpringManagedTransaction{
  // ......
  @Override
  public void commit() throws SQLException {
    if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
      LOGGER.debug(() -> "Committing JDBC Connection [" + this.connection + "]");
      this.connection.commit();
    }
  }
}
```

1. Mybatis的配置
mybatis可以通过mybatis-config.xml文件配置相关内容，其中最重要的是: environment \
environment中有两个重要的属性：
* transactionManager (用于进行事务管理)
* datasource (数据库连接池) \

因此存在以下类：
* SqlSession
* Transaction

SqlSession的创建依赖于SqlSessionFactory，\
而SqlSessionFactory的创建是使用的SqlSessionFactoryBuilder，
它通过读取mybatis-config.xml 中的相关内容进行构建。
SqlSessionFactory 也是一个接口（**抽象工厂设计模式**），\
其中主要是一个创建SqlSession的方法，
它含有两个实现类:
* DefaultSqlSessionFactory
* ManagedSqlSessionFactory
```java
public interface SqlSessionFactory{
  // ......
  SqlSession openSession();
}
```
根据上面分析的，SqlSession中主要是通过一个Executor进行最终操作，为此实现 `openSession()`
方法时，必然是需要构造相关的Executor的
```java
public class DefaultSqlSessionFactory{
  // mybatis-config.xml中的相关配置
  private Configuration configuration;
  
  @Override
  public SqlSession openSession() {
    return openSessionFromDataSource(configuration.getDefaultExecutorType(), null, false);
  }

  private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level,
      boolean autoCommit) {
    Transaction tx = null;
    try {
      // 获取 Environment 其中含有datasource  和 transactionManager的相关信息
      final Environment environment = configuration.getEnvironment();
      // 构造Transaction
      final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
      tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
      // 根据Transaction 对象构造 Executor对象
      final Executor executor = configuration.newExecutor(tx, execType);
      // 构造 SqlSession对象
      return new DefaultSqlSession(configuration, executor, autoCommit);
    } catch (Exception e) {
      closeTransaction(tx); // may have fetched a connection so lets call close()
      throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
    } finally {
      ErrorContext.instance().reset();
    }
  }
}
```



