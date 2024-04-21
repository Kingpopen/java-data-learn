### JDBC的学习
#### 1. 学习jdbc的基本使用
* 导入驱动类 (mysql 8.x 版本可以忽略这一步)
* 创建Connection
* 创建Statement
  * Statement
  * PrepareStatement: 
    1. 性能：使用这种，mysql可以执行预编译，为后续相同的sql省去了很多步骤，提高性能
    2. 安全：能够避免sql注入，它会给 占位符（？）的内容使用 ''进行包装，并且占位符内容中有
    ''会自动进行转译。
* 执行 (增删改查)
* 关闭资源 (可以使用 try-resource-finally)
```java
public class Demo{
  // 1. 导入驱动类
  Class.forName("com.mysql.cj.jdbc.Driver");
  // 2. 建立连接
  Connection connection = DriverManger.getConnection(url, root, password);
  // 3. 生成Statement
  PrepareStatement statement = connection.prepareStatement(sql);
  // 4. 执行
  ResultSet res = statement.executeQuery();
  // 5. 遍历结果
  while(res.next()){
    ......
  }
  // 6. 关闭资源
  res.close();
  statement.close();
  connection.close();
}
```
#### 2. jdbc的批处理
普通的User表（identity_id 通过UUID生成）：
```roomsql
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(100) NOT NULL COMMENT '用户姓名',
  `identity_id` varchar(100) DEFAULT NULL COMMENT '身份证号',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```
含有唯一键索引的User表：
```roomsql

CREATE TABLE `user_tmp` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(100) NOT NULL COMMENT '用户姓名',
  `identity_id` varchar(100) DEFAULT NULL COMMENT '身份证号',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key` (`username`,`address`,`identity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```
* 普通的执行流程：java客户端发送一次请求 到mysql，mysql进行执行，并返回结果。如果有多次这个请求，就要执行多个这个流程。
* 批处理执行流程： java客户端一次将多个请求(insert)，组装成一个请求(values(), (), ())，进行发送。
* 批处理的优点：
  * 当执行批处理，就可以减少网络IO的次数。
  * insert into table values(),(),()... 这种语句本身的执行效率会比较高。
* 备注： 当批量插入10w条数据时，jdbc会按照 max_allow_packet，分成多个批出跟数据库交互，而不是一次性插入10w条。

* 性能对比：
  * 普通插入 和 批量插入：
    * 普通插入10w条，耗时： `153524`ms
    * 批量插入10w条，耗时： `1495`ms
  * 唯一键索引的对比：
    * 批量插入100w条（没创建其他唯一索引），耗时：`16649`ms
    * 批量插入100w条（有创建其他唯一索引），耗时：`30151`ms
  * 有唯一键索引，ignore 和 非 ignore对比：
    * 普通插入100w条（含ignore），耗时：`26899`ms
    * 批量插入100w条（无ignore），耗时：`30151`ms
* 总结
  * 批量操作相比不是批量操作的 性能提升还是很大，我试验10w条数据就有100倍的差距，数据更大估计更明显。
  * 添加唯一键的索引之后会对性能有一定影响，特别是我的唯一键中含有一个无序的字段（UUID生成的数据），100w数据性能差距有接近2倍
    
    主要原因应该是每次都要到唯一索引树中去查找是否含有这条数据。
  * insert 使用ignore 比 不使用ignore的性能跟好。（这个原因我没理解...）

写得比较好的文章：https://www.cnblogs.com/AdaiCoffee/p/12063731.html

后续有时间需要分析一下：
- [ ] 含有唯一键索引，插入数据比较慢，是不是因为唯一键索引列中含有一个`identity_id`列的数据是无序的。 （创建一个有序列的索引再对比一下）
- [ ] 探究一下含唯一键索引的情况下，insert ignore的性能 比 普通 insert的性能还要好。
> 按照我之前的想法，在含有唯一键索引的情况下，插入数据之后，会在唯一键索引树中插入数据，但是由于identity_id列中数据是无序的，
> 
> 插入数据时可能会产生树节点的分裂，这是比较耗时的。虽然在插入时非主键索引可以异步，主键索引数据插入成功就行了，
> 
> 但是在下一次insert ignore的时候应该会查询唯一键索引树，这个时候就需要等待上一次插入的数据在唯一键索引树中是构造好的，
> 
> 这应该会比较耗时。从实验结果来看：插入100W数据，insert ignore 的效果居然比 insert 的性能更好，后续需要分析一下。
