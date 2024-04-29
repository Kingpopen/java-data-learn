## Mybatis学习
### 1. Mybatis入门
#### 1. 构建SqlSessionFactory
SqlSessionFactory是一个接口，它的默认实现是:DefaultSqlSessionFactory
创建有两种方式：
1. 使用Mybatis-config.xml进行构建
> 其中含有 Datasource 和 TransactionManager的信息
> 
> Datasource 如果需要自定义，可以实现ibatis中的接口 DataSourceFactory
* mybatis中的sqlSession默认需要commit（autoCommit 默认为false）
* jdbc中的connection默认是自动提交
在构建时依赖于SqlSessionFactoryBuilder 解析配置文件 进行构建。
2. 使用Java代码直接构建（当前我构建的方式会报错）
