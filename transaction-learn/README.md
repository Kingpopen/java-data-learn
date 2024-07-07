### transaction-learn
- [ ] 入门
- [ ] 事务
#### 1. 入门
CRUD执行异常，进行事务回滚


#### 2. Transaction失效的场景

#### 3. 保证数据一致性的方式比较
3.1 JVM锁（适合单机的场景）

3.2 数据库乐观锁 (可以支持分布式的场景)
update table_name set count = count - 1 where id = xxx and count >= 0;

3.3 数据库悲观锁 + 事务
select count from table_name where id = xxx for update;

update table_name set count = count - 1 where id = xxx;

3.4 redis分布式锁

3.5 mysql分布式锁