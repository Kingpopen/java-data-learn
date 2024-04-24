## 学习计划
### 1. jdbc-learn
学习jdbc
- [x] 基础操作
- [x] 批量操作
- [x] 连接池
- [ ] 事务的使用
### 2. mybatis-learn
java 整合 Mybatis
- [ ] 基础操作

待解决的问题：
> 上次开发使用Mybatis的sqlSession 进行批处理操作， 
> 存在下一次连接影响到了上一次连接使用的问题，我第一次连接执行插入操作，并commit了。
> 我第二次连接执行rollback操作(正好两次都是使用同一个连接)，
> 结果rollback操作将第一次插入的数据也rollback了。

### 3. spring-mybatis-learn
Spring整合mybatis
- [ ] 基础操作

### 4. spring-boot-mybatis-learn
Spring-boot 整合mybatis
- [ ] 基础操作
