###课题要求：用Java实现一个类似SQL查询的服务，参数模型自己设计
***
 ####入参：
  - 类比某张表内全量数据
  - 过滤条件，支持多个，与/或
  - 排序，支持多个，以及升序倒序
  - 分组，支持多个
  - 最大返回结果数
  
####返回：
  - 查询结果
####查询服务大概这样：
```java
    List<Object> query(List<Object> data, Where where, OrderBy orderBy, GroupBy groupBy, Limit limit) {
        //你的代码实现。。。
    }
```

####作业要求：
 1. 在个人github上新建一个工程，完成后提交该项目的URL
 2. 需要包含测试代码，测试覆盖率报告(加分项)
 3. 自由发挥，重点展现自己代码功底，包括不限于 可读性/可测性/可维护性/性能等等。有些细节如果时间紧张可以封装甚至MOCK掉。
 4. 时间要求：2-3日内，越快越好（加分项）
***
####功能完成进度如下：
- 最大返回结果数
- 过滤条件，支持多个，与/或
- 排序，支持多个，以及升序倒序
- 分组，支持多个$\color{#FF0000}{未做}$；原因：与以上功能返回结构冲突（此条返回一般为Map结构，以上功能返回List结构）>>>问了BOSS未给出答复。
####主要资源文件说明；
- 核心功能源代码：com.yangyang.example.helper.CollectionCustomQueryHelper
- 单元测试：com.yangyang.example.helper.CollectionCustomQueryHelperTest
- 测试覆盖率报告：com.yangyang.example.helper.test-coverage-report.index.html
- 数据参数模型：com.yangyang.example.entity.Device
####开发环境
- jdk1.8.0_191
- idea 2019.3
- maven apache-maven-3.6.0
