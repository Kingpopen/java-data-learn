### mybatis-springBoot-learn
#### 1. 入门
搭建流程
1. springBoot的依赖(starter)
2. 约定大于配置
> 配置datasource\
> 配置mybatis-config的信息

SpringBoot会帮忙生成：
1. SqlSessionFactory
2. SqlSessionTemplate
3. Mapper的代理实现类, 其中使用的是SqlSessionTemplate