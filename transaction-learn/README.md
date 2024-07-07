### transaction-learn
- [ ] 入门
- [ ] 数据一致性的场景

> 碎碎念：  
> 当前讲解的事务需要跟 mybatis的事务区分开来，
> 这个地方讲解的事务更多是Spring容器所支持的事务。  
> Mybatis-spring中生成的sqlSessionTemplate中含有spring的事务管理器，
> 使用sqlSessionTemplate就是将事务交给spring的事务管理器进行处理，
> 因此sqlSessionTemplate中就不支持commit, rollback, colse 等方法。  
> 但是原生的DefaultSqlSession 还是支持commit, rollback, colse 等方法  
> 如果原生的DefaultSqlSession需要添加事务，应该要怎么做呢？  
>   
> DefaultSqlSession中含有Executor 和 autoCommit的标识
> Executor有好几种的类型：simple、reuse 、batch
> BaseExecutor中有 Transaction的属性
> Transaction是个Mybatis中定义的接口，具体的实现有jdbcTransaction 和 manageTransaction ，SpringManageTransaction
> 因为Transaction接口中含有getConnection()、rollback()、commit()、colse()等方法，
> 实际上就是对Connection进行操作，为此具体的实现中都含有：DataSource、Connection、isAutoCommit的属性
> 当前发现的DefaultSqlSession.commit()是一个假的commit，
> 因为它通过SpringManageTransaction获取到的connection的autocommit是默认为true，
> 因此每一次的commit都是执行了一次stamentment的具体操作，这一次操作是自动提交的。
#### 1. 入门
事务分为编程式事务 和 声明式事务
##### 1.1 声明式事务（Spring中的Transactional 注解）
* 回滚的策略(rollbackFor) \
默认是当出现RuntimeException时就会进行回滚。
如果需要对出现unCheckException时进行回滚，需要通过rollbackFor进行指定。
```java
import java.io.IOException;

@Transactional(rollbackFor = IOException.class)
```
* - [ ] 隔离级别

* - [ ] 传播方式

* - [ ] 失效的场景

1.2 编程式事务


#### 2. Bug
> 之前使用sqlSession出现过一个Bug:  
> 线程A进行数据的批量插入，使用sqlSession.commit() 进行事务提交  
> 线程B也进行数据的批量插入，使用sqlSession.commit() 进行提交,只不过在线程B进行commit的之后，出现了一些异常  
> 进行了回滚操作，但是我发现这个回滚会将线程A的sqlSession.commit()的结果也进行了回滚，  
> 我进行问题的简单定位发现：  
> 线程A使用的sqlSession中的connection 和 线程B使用的sqlSession中的connection是同一个，但是我不太清楚为什么会产生这种错误？

##### 2.1 问题复现



#### 3. 保证数据一致性的方式比较
3.1 JVM锁（适合单机的场景）

3.2 数据库乐观锁 (可以支持分布式的场景)
update table_name set count = count - 1 where id = xxx and count >= 0;

3.3 数据库悲观锁 + 事务
select count from table_name where id = xxx for update;

update table_name set count = count - 1 where id = xxx;

3.4 redis分布式锁

3.5 mysql分布式锁