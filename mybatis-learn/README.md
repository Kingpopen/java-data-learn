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




