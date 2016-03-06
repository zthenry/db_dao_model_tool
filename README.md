这个工程是将DB中的table生成model和对应的DAO
model采用javaBean的定义规范生成
DAO是兼容Mybatis框架的数据访问层

运行过程
1.配置数据源
  在applicationContext.xml中将数据库信息配置好

2.配置需要生成的表名
  在tableName.properties中将需要生成model和DAO的表名配置好
  一个表名一行

3.配置tools.properties
  在tools.properties中需要配置如下属性:
  packagePath:生成的.java文件的包路径
  exportPath:.java文件的存放路径，配置到本地的某个dir即可
  dbReadSource:你的工程中将采用的读数据源name,这个变量将在DAO接口的mybatis的注解@DataSource中使用
  dbWriteSource:你的工程中将采用的写数据源name,这个变量将在DAO接口的mybatis的注解@DataSource中使用

4.mapping.properties
  mapping.properties中定义了mysql中的数据类型与Java中的类型的对应关系，无需配置，如果你需要新增类型，添加即可

5.执行工程，生成文件

  运行Director的main方法即可，在exportPath中去查看生成的类
