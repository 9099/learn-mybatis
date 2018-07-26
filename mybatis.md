#### 

- JDBC->Dbutils(QueryRunner)->JdbcTemplate:工具
  - 编写sql->预编译->设置参数->执行sql->封装结果
  - SQL夹在java代码中，耦合度高，硬编码
  - 维护不易，且实际开发需求中sql是有变化，需要频繁修改
- Hibernate和JPA全自动ORM框架
  - 将编写sql->预编译->设置参数->执行sql->封装结果等过程封装，开发者无需考虑

  - 长难复杂SQL，对于Hibernate言，处理也不容易
  - 内部自动生产的sql，不容易做特殊优化
  - 基于全映射的全自动框架，大量字段的POJO进行部分映射时比较困难，导致数据库性能下降

对开发者而言，核心sql还是需要自己优化

- Mybatis是一个半自动化的持久化层框架
  - 编写sql->预编译->设置参数->执行sql->封装结果等过程可以自定义sql语句
  - sql和java编码分开，功能边界清晰，一个专注业务，一个专注数据
  -  Mybatis提供的持久层框架包括SQL MAPS 和Data Access Objects(DAO)

#### 实例

hello world

创建数据表

```sql
CREATE DATABASE IF NOT EXISTS mybatis DEFAULT CHARSET utf8;
use mybatis;
CREATE TABLE `tbl_employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_name` varchar(255) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

创建bean

```java
package com.snomis.mybatis.bean;

public class Employee {
    private Integer id;
    private String lastName;
    private String email;
    private String gender;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
```

创建mybatis全局配置文件 mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING" />
    </settings>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=false"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="EmployeeMapper.xml"/>
    </mappers>
</configuration>
```

创建映射文件 EmployeeMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.snomis.mybatis.EmployeeMapper">
    <select id="selectEmp" resultType="com.snomis.mybatis.bean.Employee">
        select * from tbl_employee where id = #{id}
    </select>
</mapper>
```

创建 log4j配置文件

```properties
# Configure logging for testing: optionally with log file
log4j.rootLogger=WARN, stdout
# log4j.rootLogger=WARN, stdout, logfile

log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] - %m%n

log4j.appender.logfile=org.apache.log4j.FileAppender
log4j.appender.logfile.File=target/spring.log
log4j.appender.logfile.layout=org.apache.log4j.PatternLayout
log4j.appender.logfile.layout.ConversionPattern=%d %p [%c] - %m%n

log4j.logger.*=DEBUG
```

创建测试类

```java
package com.snomis.mybatis.test;

import com.snomis.mybatis.bean.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MybatisTest {

    /**
     * 1.创建mybatis-config.xml
     * 2.创建sql映射文件，配置每个sql以及sql映射规则
     * 3.将sql映射文件注册到全局配置文件中
     * 4.创建测试类
     *      1.根据mybatis-config.xml全局配置文件创建SqlSessionFactory对象
     *      2.使用工厂对象获取一个会话 session，用完需要关闭
     *      3.用session中提供的方法进行增删改查
     * @throws IOException
     */
    @Test
    public void test() throws IOException{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession openSession = sqlSessionFactory.openSession();

        Employee e = (Employee)openSession.selectOne("com.snomis.mybatis.EmployeeMapper.selectEmp", 1);
        System.out.println(e);
        openSession.close();
    }
}
```

#### Mapper接口与配置文件绑定

修改mapper文件 EmployeeMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace写接口的全类名-->
<mapper namespace="com.snomis.mybatis.dao.EmployeeMapper">
    <select id="getEmpById" resultType="com.snomis.mybatis.bean.Employee">
        select * from tbl_employee where id = #{id}
    </select>
</mapper>
```

创建接口

```java
package com.snomis.mybatis.dao;

import com.snomis.mybatis.bean.Employee;

public interface EmployeeMapper {
    public Employee getEmpById(Integer id);
}
```

Mapper接口没有实现类但是mybatis会自动创建一个代理对象

编写测试类

```java
package com.snomis.mybatis.test;

import com.snomis.mybatis.bean.Employee;
import com.snomis.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MybatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void test01() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
        Employee e = mapper.getEmpById(1);
        System.out.println(e);
        openSession.close();
    }
}
```

要注意：

- SqlSession代表和数据库的一次会话，用完必须关闭
- SqlSession是非线程安全的，每次使用都应该获取新对象，不能共享成员变量
- mapper接口没有实现类，但是mybatis会为接口生成一个代理对象
- 有两个重要的配置文件
  - mybatis的全局配置文件，包括数据库连接池信息，事务管理器，系统运行环境等
  - sql映射文件

#### 全局配置文件mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!-- 使用properties引入外部properties配置文件，比如数据源信息等 
        1、resource属性：引入类路径下的配置
        2、url属性：引入磁盘或者网络路径下的配置文件

        如配置文件中写：
        jdbc.driver=com.mysql.jdbc.Driver
        jdbc.url=jdbc:mysql://localhost:3306/mybatis
        jdbc.username=root
        jdbc.password=root

        然后用resource属性引入，写法是 resource="包路径/文件名""

        然后就可以在这个xml配置文件中使用 ${变量名} 来引用变量了
        如:
        <property name="username" value="${jdbc.username}"/>
    -->
    <properties resource="" ></properties>
    
    <!-- 设置项 
        有许多设置项，如：
        cacheEnabled 是否缓存
        mapUnderCaseToCamelCase 是否启用驼峰命名规则

        属性：
        1、name：设置项名字
        2、value：设置的值
        也可以在类上使用@Alias注解
    -->
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING" />
        <!-- 延迟加载 -->
        <setting name="lazyLoadingEnabled" value="true" />
        <!-- true：所有延迟属性同时加载，false：所有延迟属性按需要加载-->
        <setting name="aggressiveLazyLoading" value="false" />
    </settings>

    <!-- 别名设置 
        属性：
        typeAlias为某个类起别名
        1、type：指定要重命名的类型
        2、alias: 指定别名，不指定则默认为类名的小写
        package为包批量取别名
        属性
        1、name：包名，该包下的所有类的别名都是类名小写
    -->
    <typeAliases>
        <typeAlias type="me.snomis.helloworld.bean.Employee" alias="emp">
        <package name="me.snomis.helloworld.bean">
    </typeAliases>
    
    <!-- 设置类型转换处理器 
        因为不同的类型可能有不同的转换方式
        java1.8之后添加了JSR-310标准(Date and Time API),增加了时间日期处理库
        低版本mybatis没有这些库，附加的处理方法可以在mybatis-typehandlers-jsr310中找，用以下标签注册
        mybatis3.4以后已经集成了这些方法，不需要再注册
    -->
    <typeHandlers>
        <typeHandler handler="org.apache.ibatis.type.InstantTypeHandler" />
        <typeHandler handler="org.apache.ibatis.type.LocalDateTimeTypeHandler" />
    </typeHandlers>

    <plugins>
                
    </plugins>

    <!-- 环境的配置，mybatis可以配置多种环境
        environments标签下有environment标签，用于配置不同的环境，如测试和开发等环境
        在environments的default属性切换不同的环境
        
        environment标签需要有1个id属性标明环境，同时必须有一下两个子标签：
            1、transactionManager(必须有) 事务管理
                属性：
                    type：可以是JDBC或MANAGED，JDBC带包JDBC方式，MANAGED方式是让服务器容器自己管理，还可以自己定义事务管理器，实现TransactionFactory接口即可，spring有事务管理，所以不用配置这个
            2、dataSource(必须有) 
                属性:
                    type: 连接池，可以是UNPOOLED或者POOLED或者JNDI，可以自定义，只要实现DataSourceFactory，type是全类名
     -->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=false"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>

    <!-- 根据不同的数据库使用不同的语句 
        属性：
        type: DB_Vendor 得到数据厂商标识(驱动自带的)，根据标识执行不同的语句
        MySQL， Oracle， SQL Server， 
    -->
    <databaseIdProvider type="DB_VENDOR">
        <!-- 给标识取别名 
            然后在mapper文件中在select标签的databaseId属性中指定数据库厂商标识
            mybatis在加载的时候会加载符合标识的配置
        -->
        <property name="MySQL" value="mysql"/>
    </databaseIdProvider>

    <!-- 将sql映射注册到全局配置中 -->
    <mappers>
        <!-- 注册一个sql映射
            属性：
            1、resource：引用类路径下的sql映射文件
            2、url：引用磁盘网络路径下的sql映射文件
            3、class：注册接口，前两种是注册配置文件的，这个属性可以直接写接口的全类名，这种方式要求配置的xml映射文件和接口的java文件放在同一目录下，并且命名相同，也可以基于注解的方式，即在接口上添加注解
            @Select("select * from tbl_employee where id=#{id}")

         -->
        <mapper resource="EmployeeMapper.xml"/>
        <mapper class=""/>
        <mapper url=""/>

        <!-- package：name中写包名批量注册
            直接写包名会找不到关联的mapper映射文件，所以需要映射文件和接口文件放在同一目录下使用相同的命名，或者使用注解的方式
         -->
        <package name=""/>
    </mappers>
</configuration>
```

#### 增删改

EmployeeMapper.java

```java
package me.rsnomis.dao;

import me.rsnomis.bean.Employee;

public interface EmployeeMapper {

    Employee getEmpById(Integer id);

    /**
     * mybatis允许Integer Long Boolean等返回值，返回操作信息，
     * 直接在函数前修改返回值类型即可
     * @param employee
     */
    Boolean addEmp(Employee employee);

    void updateEmp(Employee employee);

    void deleteEmpById(Integer id);
}
```

mybatis允许Integer、Long、Boolean、void等返回值，返回操作信息，直接在函数前修改返回值类型即可

EmployeeMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace写接口的全类名-->
<mapper namespace="me.rsnomis.dao.EmployeeMapper">
    <select id="selectEmp" resultType="me.rsnomis.bean.Employee">
        select id,last_name lastName, email, gender from tbl_employee where id = #{id}
    </select>

    <select id="getEmpById" resultType="me.rsnomis.bean.Employee">
        select id,last_name lastName, email, gender from tbl_employee where id = #{id}
    </select>

    <!--mybatis支持自增主键值的获取，利用statement.getGeneratedKeys()
    设置useGeneratedKeys为true 使用获取主键值的策略
    指定对应的主键属性，即获取主键值后封装到对象的什么属性上
    -->
    <insert id="addEmp" parameterType="me.rsnomis.bean.Employee"
        useGeneratedKeys="true" keyProperty="id" databaseId="MySQL">
        insert into tbl_employee(last_name,email,gender)
        values(#{lastName}, #{email}, #{gender})
    </insert>

    <!--Oracle中的插入，主键是从序列中拿到的
        可以在selectKey标签中先定义查找id的语句
    -->
    <insert id="addEmp" parameterType="me.rsnomis.bean.Employee"
            databaseId="Oracle">
        <!--keyProperty：属性值封装给那个bean属性
            order: BEFORE 因为是在插入之前先查询主键值，所以设定执行顺序为之前执行
                   AFTER 见下面AFTER的语句，即先运行插入，然后插入执行完后再获取id
            resultType: 说明封装的字段类型
        -->
        <selectKey keyProperty="id" order="BEFORE" resultType="Integer">
            <!--编写主键查询sql语句，是在插入之前运行的-->
            select EMPLOYEE_SEQ.nextval from dual

            <!--使用after时，先执行了插入语句，可以使用以下语句获取id值
            mysql也可以使用这种方式，直接select id
            AFTER的方式在同时插入多条数据的时候可能取到相同值，一般用BEFORE
            select EMPLOYEE_SEQ.currval from dual
            -->
        </selectKey>
        <!--插入从序列中拿到的id，变量名即keyProperty中定义的-->
        insert into tbl_employee(EMPLOYEE_ID,LAST_NAME,EMAIL)
        values(#{id}, #{lastName}, #{email}, #{gender})

        <!--如果order使用AFTER，则先执行如下语句，此时id序列号是直接在插入语句的同时获取的，插入完成后再获取id值
        insert into tbl_employee(EMPLOYEE_ID,LAST_NAME,EMAIL)
        values(EMPLOYEE_SEQ.nextval, #{lastName}, #{email}, #{gender})-->
    </insert>
    
    <update id="updateEmp" parameterType="me.rsnomis.bean.Employee">
        update tbl_employee
            set last_name=#{lastName}, email=#{email}, gender=#{gender}
            where id=#{id}
    </update>
    
    <delete id="deleteEmpById" parameterType="me.rsnomis.bean.Employee">
        delete from tbl_employee where id=#{id}
    </delete>

</mapper>
```

mybatis支持自增主键值的获取

对于mysql等支持自增造作的数据库，INSERT操作若不指定主键，则主键值需要在添加之后由数据库生成才能被用户获取，配置步骤：

1. 在mapper文件中设置useGeneratedKeys为true 使用获取主键值的策略
2. 用keyProperty指定对应的主键属性，即获取主键值后封装到对象的什么属性上
3. 然后就可以从对象中获取到主键值了



Oracle不支持自增主键，使用序列模拟自增，每次插入的主键是从序列中获得的

插入数据的过程是：

1. 从Oracle序列表中获取下一个序列值
2. 再插入数据

配置时，需要现在selectKey中配置序列查询语句，然后再用查到的值设置id



#### Mybatis参数传递的规则

- 单个参数：在接口函数中，若只写了一个参数，那么Mybatis不会做特殊处理，参数名可以任意取

		\#{参数名} 参数名随便写，因为只有一个参数

- 多个参数：在接口函数中，若写了多个参数，会做特殊处理，多个参数会被封装为一个map，#{}则是在map中取值，map的构成是：

key: param1 ... paramN

value: 传入的参数值

可以使用：

​	\#{arg0}, \#{arg1} ...

​	\#{param1}, \#{param2}...

​	旧版本可以使用: \#{0}, \#{1}

可以命名参数：

明确指定封装为map后的key值，方法是在接口中使用@Param("id")参数，这样就可以直接在xml映射文件中使用#{变量名}这样的方式了

```java
package me.rsnomis.dao;

import me.rsnomis.bean.Employee;

public interface EmployeeMapper {

    Employee getEmpByIdAndLastName(@Param("id")Integer id, @Param("last_name")String last_name);
    
}
```

如果在全局配置文件中配置了useActualParamName那么也可以直接使用参数名(需要jdk1.8之后)

- 如果参数很多，且是业务逻辑的数据模型，可以直接用pojo

		\#{属性名}，取出传入的pojo

- 如果多个参数没pojo封装，可以传入map

		\#{key}，就是取出map中的键名

- 如果有多个参数，经常使用map也不方便，可以封装一个TO(Transfer Object)数据传输对象

#### 取值的案例

```java
public Employee getEmp(@Param("id")Integer id, String lastName);
```

取值： 

- id ==> \#{id} 或 #{param1} 或 \#{arg0} 老版本 \#{0}
- lastName ==> \#{param2}

```java
public Employee getEmp(Integer id, @Param("e")Employee emp);
```

取值：

- id==>\#{param1}
- lastName ==> \#{param2.lastName} 或 \#{e.lastName} 

```java
public Employee getEmp(List<Integer> ids);
```

注意如果传入的参数是Collection(List、Set)类型，或者是数组，也会特殊处理，把传入的list或是数组封装在map中，map中的key是collection，如果是List那么key是list，如果是Array那么就是array

取值：

- 取出第一个id的值：\#{list[0]}

#### 参数值的获取#和$的区别

区别是：

- \#号是使用?做sql的占位符，相当于用jdbc的PreparedStatement 的预编译方式
- ?号则是直接将参数拼接到sql语句上

如：

```sql
select * from tbl_employee where id=${id} and last_name=#{lastName}
```

mybatis在处理的时候，会这样处理，先预处理sql语句：

```sql
## 假设参数id为2
select * from tbl_employee where id=2 and last_name=？
```

所以使用$容易被sql注入

使用$的情况下:

- 一般在拼接变量名的时候可以使用
- 在order by之后

原理是：

PreparedStatement会对SQL进行了预编译，在第一次执行SQL前数据库会进行分析、编译和优化，同时执行计划同样会被缓存起来，它允许数据库做参数化查询。在使用参数化查询的情况下，数据库不会将参数的内容视为SQL执行的一部分，而是作为一个字段的属性值来处理，这样就算参数中包含破环性语句（or ‘1=1’）也不会被执行。 

而order by之后的参数并不是字段属性值，所以只能通过$来把变量当成sql语句的一部分，先组合到sql语句中去

#### Null值的处理

\#{}有更丰富的用法：

规定参数的规则：

javaType、jdbcType、mode(存储过程)、numericScale

resultMap、typeHandler、jdbcTypeName、expression

其中jdbcType通常在某些情况下要设置，在数据为null值的时候有些数据库不能识别mybatis对null的默认值，比如Oracle

mybatis中JdbcType中的OTHER类型表示无效类型，mybatis对所有的null值都映射为原生Jdbc的OTHER类型，这是Oracle不支持的，Mysql能支持

此时可以特别指定jdbcType，用法如下：

```sql
insert into employees(EMPLOYEE_ID,LAST_NAME,EMAIL) values(#{id}, #{lastName}, #{email, jdbcType=NULL})
```

也可以在全局配置文件中设置jdbcTypeForNull为NULL

#### resultType的使用

- 若返回的是List等集合，那么resultType中需要写list中对象的全路径
- 若要返回一个map，key是数据表的列名，value是对应的值，每次查询一条数据返回封装的map那么，resultType填map，map是mybatis内部定义的的别名
- 如果想返回一个封装多条数据的map，如Map<String, Employee>,那么resultType填写的是map中value的全类名

#### ResultMap的使用

```sql
## 创建部门表
CREATE TABLE tbl_dept(
	id INT(11) PRIMARY KEY AUTO_INCREMENT,
	dept_name VARCHAR(255)
)ENGINE=INNODB DEFAULT CHARSET=utf8

ALTER TABLE tbl_employee ADD COLUMN d_id INT(11)
```

**基本使用**

可以使用resultMap指定表的列名和bean属性名的对应关系

```xml
	<resultMap id="MyEmp" type="me.rsnomis.bean.Employee">
        <id column="id" property="id" />
        <result column="last_name" property="lastName" />
        <result column="email" property="email" />
        <result column="gender" property="gender" />
    </resultMap>
    <select id="getEmpById" resultMap="MyEmp">
        select * from tbl_employee where id = #{id}
    </select>
```

其中，id特指主键，也可以用result，但是id标签可以对主键进行优化处理

**联合查询**

在进行联合查询的时候，比如在查出人物信息的时候还要查人物的部门，可以在Employee对象中添加一个名为dept的属性，封装Department对象

在resultMap下result的property属性中使用级联属性指明：

```xml
<resultMap id="EmpAndDept" type="me.rsnomis.bean.Employee">
        <id column="id" property="id" />
        <result column="last_name" property="lastName" />
        <result column="d_id" property="dept.id" />
        <result column="gender" property="gender" />
        <result column="dept_name" property="dept.departmentName" />
    </resultMap>
    <select id="getEmpAndDept" resultMap="EmpAndDept">
        select e.id id, e.last_name last_name, e.gender gender, e.d_id d_id, d.id did, d.dept_name dept_name from tbl_employee e,ebl_dept d where e.d_id = d.id and e.id=#{id}
    </select>
```

如上代码中用`.`号来级联属性



除了用级联属性的方式，还可以用association标签

```xml
<resultMap id="EmpAndDeptDif" type="me.rsnomis.bean.Employee">
        <id column="id" property="id" />
        <result column="last_name" property="lastName" />
        <result column="gender" property="gender" />

        <!--association可以用来指定联合查询的对象
                property: 级联对象的对象名
                javaType：级联对象的全类名,不能省略
            标签内和resultMap一样写，column是列名，property是对象的属性名
        -->
        <association property="dept" javaType="me.rsnomis.bean.Department">
            <id column="d_id" property="id" />
            <result column="dept_name" property="departmentName" />
        </association>

    </resultMap>
    <select id="getEmpAndDeptDif" resultMap="EmpAndDeptDif">
        select e.id id, e.last_name last_name, e.gender gender, e.d_id d_id, d.id did, d.dept_name dept_name from tbl_employee e,tbl_dept d where e.d_id = d.id and e.id=#{id}
    </select>
```

association可以用来指定联合查询的对象

- property: 级联对象的对象名
- javaType：级联对象的全类名,不能省略
- 标签内和resultMap一样写，column是列名，property是对象的属性名

**分步查询**

现在有一个DepartmentMapper和一个EmployeeMapperPlus，分别有两个查询

```sql
select * from tbl_employee where id = 1
select * from tbl_dept where id = 1
```

现在要执行以下两个步骤：

1. 根据员工id查询Employee信息，获得部门id
2. 用DepartmentMapper的getDeptById查得部门信息
3. 把查出的部门信息，设置到employee的对象的dept属性中

此时也可以用association标签，要用到两个属性：

1. select：值就是调用的查询方法写法是方法的： 类名+方法名
2. column: 前一个查询结果中要传入的列名

```xml
<!--分步查询：分步执行以下步骤
        1. 根据员工id查询Employee信息，获得部门id
        2. 用DepartmentMapper的getDeptById查得部门信息
        3. 把查出的部门信息，设置到employee的对象的dept属性中
    -->
    <resultMap id="EmpAndDeptStep" type="me.rsnomis.bean.Employee">
        <id column="id" property="id" />
        <result column="last_name" property="lastName"/>
        <result column="email" property="email" />
        <result column="gender" property="gender"/>
        <!--用association定义employee对象中部门属性dept的关联对象的封装规则
            现在dept的数据是要根据部门id查处来的，部门id值是前一个查询查出来的
            select：值就是调用的查询方法写法是方法的： 类名+方法名
            column: 前一个查询结果中要传入的列名
        -->
        <association property="dept"
                     select="me.rsnomis.dao.DepartmentMapper.getDeptById"
                     column="d_id">
        </association>
    </resultMap>
    <select id="getEmpByIdStep" resultMap="EmpAndDeptStep">
        select * from tbl_employee where id=#{id}
    </select>
```

**延迟查询**

分步查询中先查Employee再查dept，但是目前为止在每次查询Employee对象的时候，都同时查了部门。延迟加载可以实现先查员工，部门等要用的时候再查询，这样就节约的数据库的开销
配置延迟查询只需在分步查询的基础上做以下配置：
        1. 在全局配置文件中的setting中设置lazyLoadingEnabled为true
        2. 在全局配置文件中的setting中设置aggressiveLazyLoading为false

#### 一对多的查询

之前讲的是关联一个对象，一对一，如果在部门的对象中添加员工列表的属性，表示这个部门的所有员工，要查询属于这个部门的所有员工，就一个部门对多个员工的问题

此时可以在resultMap中使用collection标签，封装所有的Employee对象

属性有两个：

1. property: 指定集合属性的属性名
2. ofType: 指定集合内部对象的类型

```xml
<!--要查询一个部门的所有员工，一对多的关系
        在部门对象中，用集合存储所有的员工
        这里用collection定义集合：
        1. property: 指定集合属性的属性名
        2. ofType: 指定集合内部对象的类型
    -->
    <resultMap id="MyDept" type="me.rsnomis.bean.Department">
        <id column="did" property="id"/>
        <result column="dept_name" property="departmentName"/>
        <collection property="emps" ofType="me.rsnomis.bean.Employee">
            <id column="eid" property="id"/>
            <result column="last_name" property="lastName"/>
            <result column="email" property="email"/>
            <result column="gender" property="gender"/>
        </collection>
    </resultMap>
    <select id="getDeptByIdPlus" resultMap="MyDept">
        select d.id did, d.dept_name dept_name,
                e.id eid, e.last_name last_name, e.email email, e.gender gender
        from tbl_dept d left join tbl_employee e
        on d.id = e.d_id
        where d.id = #{id}
    </select>
```

**一对多的情况下用分步查询**

分步查询也分两步，先查部门信息，再查员工信息

```
select * from tbl_dept where id = 1
select * from tbl_employee where d_id = 1
```

collection的分步查询使用方法和association类似，同样也支持延迟加载

```xml
<!--分步查询，collection也有分步查询
        同样设置两个属性
        1. select：定义的EmployeeMapperPlus中的getEmpByDeptId方法
        2. column：传入的参数在之前查询的列名
        3. fetchType: 设置默认采用延迟加载，这个属性在association中也有，设置成eager就取消了延迟加载，覆盖全局配置中的设置
        这也支持延迟加载
    -->
    <resultMap id="MyDeptStep" type="me.rsnomis.bean.Department">
        <id column="id" property="id"/>
        <result column="dept_name" property="departmentName"/>
        <collection property="emps"
                    select="me.rsnomis.dao.EmployeeMapperPlus.getEmpByDeptId"
                    column="id"
                    fetchType="lazy">
        </collection>
    </resultMap>
    <select id="getDeptByIdStep" resultMap="MyDeptStep">
        select id, dept_name from tbl_dept where id = #{id}
    </select>
```

如果分步查询要传入多个column变量，可以将多列数据封装为map传递

column样例如下：

```
column="{key1=column1,key2=column2}"
```

本例中：

```
column="{deptId = id}"
```

因为在me.rsnomis.dao.EmployeeMapperPlus.getEmpByDeptId的map映射文件中使用了的是

```
select * from tbl_employee where d_id = #{deptId}

```

所以key是deptId或者arg[0]或者param[1]

**鉴别器**

```xml
<!--鉴别器：
        <discriminator javaType=""></discriminator>
        mybatis可以使用discriminator判断某列的值，然后根据某列的值改变封装行为
        比如封装Employee：
            如果查出的是女生：就把部门信息查询出来，否则不查询
            如果查出的是男生：就把last_name一列的值赋值给email
    -->
    <resultMap id="MyEmpDis" type="me.rsnomis.bean.Employee">
        <id column="id" property="id" />
        <result column="last_name" property="lastName"/>
        <result column="email" property="email" />
        <result column="gender" property="gender"/>
        <!--column指定要判断的列和类型-->
        <discriminator javaType="string" column="gender">
            <!--女生封装规则，需要写封装对象-->
            <case value="0" resultType="me.rsnomis.bean.Employee">
                <association property="dept"
                             select="me.rsnomis.dao.DepartmentMapper.getDeptById"
                             column="d_id">
                </association>
            </case>
            <!--男生封装规则，需要写封装对象-->
            <case value="1" resultType="me.rsnomis.bean.Employee">
                <id column="id" property="id" />
                <result column="last_name" property="lastName"/>
                <result column="last_name" property="email" />
                <result column="gender" property="gender"/>
            </case>
        </discriminator>
    </resultMap>
    <select id="getEmpByIdDis" resultMap="MyEmpDis">
        select id, last_name, gender, email, d_id from tbl_employee where id = #{id}
    </select>
```

#### 动态sql标签

`if`标签可以根据实际数据进行判断是否添加查询条件，test使用的是ognl表达式

```xml
<!--if标签
    -->
    <select id="getEmpsByConditionIf" resultType="me.rsnomis.bean.Employee">
        select * from tbl_employee
        where
        <!--使用ognl表达式
            ognl表达式中特殊字符早转义，可以查iso-8859-1的特殊字符实体
        -->
        <if test="id!=null">
            id = #{id}
        </if>
        <if test="lastName!=null and lastName!=''">
            and last_name like #{lastName}
        </if>
        <if test="email!=null &amp;&amp; email.trim()!=&quot;&quot;">
            and email = #{email}
        </if>
        <!--ognl会自动转换字符串和数字-->
        <if test="gender==0 or gender==1">
            and gender = #{gender}
        </if>
    </select>
```

只用`if`那么`sql`语句中可能会多出来`and`等关键字

解决办法：

1. 在where之后添加1=1
2. 使用where标签

`where`标签

where标签可以自动去除多余的and，但是只能去除写在前面的and，写在后面的and不能去除

```xml
<!--where标签
    -->
    <select id="getEmpsByConditionWhere" resultType="me.rsnomis.bean.Employee">
        select * from tbl_employee
        <where>
            <!--使用ognl表达式
                ognl表达式中特殊字符早转义，可以查iso-8859-1的特殊字符实体
            -->
            <if test="id!=null">
                id = #{id}
            </if>
            <if test="lastName!=null and lastName!=''">
                and last_name like #{lastName}
            </if>
            <if test="email!=null &amp;&amp; email.trim()!=&quot;&quot;">
                and email = #{email}
            </if>
            <!--ognl会自动转换字符串和数字-->
            <if test="gender==0 or gender==1">
                and gender = #{gender}
            </if>
        </where>
```

trim标签可以智能的去除前后多余的关键字

```xml
<!--trim语句测试
        where不能解决句末的and，trim可以
        1. prefix: 前缀,trim标签体中整个字符串拼串后的结果，在结果中加一个前缀
        2. prefixOverrides: 去掉标签中前面多余的字符
        3. suffix: 给标签体中拼串的结果加一个后缀
        4. suffixOverrides: 去掉标签体后多余的后缀
    -->
    <select id="getEmpsByConditionTrim" resultType="me.rsnomis.bean.Employee">
        select * from tbl_employee
        <trim prefix="where" suffixOverrides="and" prefixOverrides="and">
            <if test="id!=null">
                id = #{id} and
            </if>
            <if test="lastName!=null and lastName!=''">
                and last_name like #{lastName} and
            </if>
            <if test="email!=null &amp;&amp; email.trim()!=&quot;&quot;">
                and email = #{email} and
            </if>
            <!--ognl会自动转换字符串和数字-->
            <if test="gender==0 or gender==1">
                and gender = #{gender} and
            </if>
        </trim>
    </select>
```

choose

```xml
<!--choose标签相当于switch case 分支选择
        测试如果带了id就用id查，如果带了last_name就用last_name查
        每次只能进入一个分支
    -->
    <select id="getEmpsByConditionChoose" resultType="me.rsnomis.bean.Employee">
        select * from tbl_employee
        <where>
            <choose>
                <when test="id!=null">
                    id=#{id}
                </when>
                <when test="lastName!=null">
                    last_name like #{lastName}
                </when>
                <when test="email!=null">
                    email=#{email}
                </when>
                <otherwise>
                    gender = 0
                </otherwise>
            </choose>
        </where>
    </select>
```

set

```xml
<!--更新数据如果传入对象，那么一般就全都更新，不管是否对象的属性是否有值
        set可以实现哪一个属性有值就更新哪一个属性
        如果如下代码不用set标签，直接用set，会导致有的多余逗号不能自动去除
        使用set标签可以智能去除多余的逗号
        用trim标签也可以实现set的功能
    -->
    <update id="updateEmpByIdSet" >
        update tbl_employee
        <set>
            <if test="lastName!=null">
                last_name=#{lastName},
            </if>
            <if test="gender!=null">
                last_name=#{gender},
            </if>
            <if test="email!=null">
                last_name=#{email},
            </if>
        </set>
        where id = #{id}
    </update>
```

foreach

```xml
<!--foreach将遍历出的元素赋值给变量
        1.collection: 指定遍历的集合，list类型的参数会特殊处理封装在map中，map的key就叫list
        2.item: 将当前遍历出的元素赋值给指定的变量
        3.separator: 遍历出的元素的分割符
        4.open: 拼接开始的字符
        5.close: 拼接结束的字符
        6.index: 遍历list时 index是索引，item是值
                 遍历map时 index是key， item是值
        #{变量名} 就能取出变量的值也就是当前遍历出的元素

        collection的取值：
                    1.若传入的是list: 可以直接写list
                    2.若传入的是array: 可以直接写array
                    3.传入对象，对象中有list: 可以直接写oredCriteria，mybatis会找到非集合对象中的集合对象
    -->
    <select id="getEmpsByConditionForeach" resultType="me.rsnomis.bean.Employee">
        select * from tbl_employee where id in
        <foreach collection="ids" item="item_id" separator="," open="(" close=")">
            #{item_id}
        </foreach>

    </select>
```

批量保存

```xml
<!--批量添加参数-->
    <insert id="addEmps" >
        insert into tbl_employee(last_name, email, gender, d_id) values
        <foreach collection="emps" item="emp" separator=",">
            (#{emp.lastName}, #{emp.email}, #{emp.gender}, #{emp.dept.id})
        </foreach>
    </insert>
```

内置参数

```xml
<!--内置参数
        不只是方法传递过来的参数可以被用来判断和取值，
        mybatis还有两个内置参数
        _parameter: 代表整个参数
            传入单个参数，那么_parameter就是这个参数
            传入多个参数，参数会被封装为一个map，_parameter就是这个map
        _databaseId: 如果配置了DatabaseIdProvider标签，那么_databaseId就是当前数据库的别名

        如果传入单个基本类型，则if标签一定要用_parameter做判断
        传入的是对象，if标签做判断用对象的属性名
        若传入多个对象，则需要使用_parameter.get("0")获取到对象

    -->
    <select id="getEmpsInnerPatameter" resultType="me.rsnomis.bean.Employee">
        <!--select * from tbl_employee where id=#{_parameter.id}-->
        <if test="_databaseId=='mysql'">
            select * from tbl_employee
            <if test="_parameter!=null">
                where id=#{_parameter.id}
            </if>
        </if>
        <if test="_databaseId=='Oracle'">
            select * from employees
        </if>
    </select>
```

bind

```xml
<!--bind标签
        模糊查询的时候如果参数中写
        select * from tbl_employee where last_name like "%#{lastName}%"
        这样是不行的，因为参数没加进去的时候已经编译好sql语句了
        如果这样写可以，但是容易引起注入
        select * from tbl_employee where last_name like "%${lastName}%"
        这时候可以使用bind标签
    -->
    <select id="getEmpsByNameBind" resultType="me.rsnomis.bean.Employee">
        select * from tbl_employee
        <bind name="_lastName" value="'%'+name+'%'"/>
            where last_name like #{_lastName}
    </select>
```

sql

```xml
<!--sql标签用于抽取可重用的sql拍那段方便引用
        用include引用即可
        在include标签中还可以用property自定义变量
    -->
    <sql id="empId">
        id = #{id}
    </sql>
    <select id="getEmpByIdSQL" resultType="me.rsnomis.bean.Employee">
        select * from tbl_employee where
        <include refid="empId">
            <property name="idname" value="idvalue"></property>
        </include>
    </select>
```

#### 缓存机制

Mybatis包含一个非常强大的查询缓存特性，可以非常方便的配置和定制。缓存可以极大的提升查询效率。

- mybatis系统中默认定义了两级缓存
- 一级缓存和二级缓存
  1. 默认情况下，只有一级缓存(SqlSession级别的缓存，也称为本地缓存)开启
  2. 二级缓存需要手动开启和配置，是基于namespace级别的缓存
  3. 为了提高扩展性，Mybatis定义了缓存接口Cache，我们可以通过实现Cache接口来自定义二级缓存

很多固定不变的数据可以缓存，这样就不用多次查询数据库，提升系统性能，比如菜单数据等

**一级缓存**

一级缓存是本地缓存，数据库同一次会话期间查询到的数据会放在本地缓存中，以后若要获取相同的数据，直接从缓存中获取，不用查数据库。

在sqlSession关闭前，查询会被缓存，在第二次执行相同的查询的时候，会直接从缓存中读取数据。所以每个sqlSession都有自己的一级缓存，不同的sqlSession之间不能共享，关闭sqlSession，缓存也销毁。

一级缓存是一直开启的，不能关闭。

一级缓存失效的情况：

- sqlSession不同
- sqlSession相同时，查询条件不同
- 相同的sqlSession，相同的查询条件，但是在两次查询之间有增删改操作（不论sqlSession是否commit）
- 相同的sqlSession，相同的查询条件，但是手动清空了一级缓存

SqlSession级别的一级缓存其实是一个map，每次查询，通过一个map保存数据





**二级缓存**

二级缓存是全局缓存，一级缓存作用范围小，比如不变的网站目录等信息，如果session关闭，缓存就没了，这样性能也不好，所以可以使用二级缓存，二级缓存是基于namespace级别的，每个namespace对应一个二级缓存

工作机制：

1. 一个会话，查询一条数据，这个数据就放在会话的一级缓存中
2. 如果当前会话关闭了，一级缓存失效，但是一级缓存中的数据并没有清空，而是保存到了二级缓存中，查询相同的信息就可以从二级缓存中查找
3. SqlSession通过EmployeeMapper查得Employee，又通过DepartmentMapper查得Department，此时两个查询结果被放在两个不同的二级缓存中，因为namespace不同，不同namespace的二级缓存数据不共享

注意：缓存是先存在一级缓存中的，只有session关闭后才会进入二级缓存，session如果不关闭，数据就不会进入二级缓存

使用步骤：

1. 开启二级缓存的配置：在全局配置文件中的setting中开启cacheEnabled
2. 取mapper.xml中配置使用二级缓存：在mapper标签下添加`<cache></cache>`标签
3. POJO需要实现序列化接口，因为返回数据可能用到序列化和反序列化

cache标签的属性：

- eviction缓存的回收策略：
  - LRU - 最近最少使用的，移除最长时间不被使用的对象
  - FIFO - 先进先出，按对象进入缓存的顺序来移除
  - SOFT - 软引用，移除基于垃圾回收器状态和软引用规则的对象
  - WEAK - 弱引用，更积极的移除基于垃圾收集器状态和弱引用规则的对像
  - 默认的是LRU
- flushInterval缓存刷新间隔，多长时间缓存清空一次，默认不清空，可以设置一个毫秒值
- readOnly 是否只读
  - true - mybatis认为所有从缓存中获取数据的操作都是只读，为了加快获取速度，就直接将缓存中的数据的引用返回
  - false - mybatis认为操作可能会被更改，会使用序列化和反序列化克隆一份数据
- size 缓存存放的元素个数
- type 指定自定义缓存的全类名，实现cache接口即可

```xml
<!--启用二级缓存
        1.eviction: 回收策略，回收什么缓存
        - LRU - 最近最少使用的，移除最长时间不被使用的对象
        - FIFO - 先进先出，按对象进入缓存的顺序来移除
        - SOFT - 软引用，移除基于垃圾回收器状态和软引用规则的对象
        - WEAK - 弱引用，更积极的移除基于垃圾收集器状态和弱引用规则的对像
        - 默认的是LRU

        2.flushInterval: 缓存刷新间隔，多长时间缓存清空一次，默认不清空，可以设置一个毫秒值
        3.readOnly: 是否只读，只读返回数据引用，非只读返回数据副本
        4.size: 表示缓存中存储多少数据个数
        5.type: 用于自定义缓存，写自定义缓存的全类名，实现cache接口就行
    -->
    <cache eviction="FIFO" flushInterval="6000" readOnly="true" size="1024"></cache>
```

**和二级缓存有关的设置和属性**

1. cacheEnabled=true； fasle  关闭的是二级缓存，无法关闭一级缓存
2. 在select标签上添加useCache属性：fasle是不是用缓存，只能关闭二级缓存
3. 每个增删改标签上都有一个标签flushCache：默认为true，即每次增删改都会清除一级缓存，同时也会清除二级缓存，在每个select标签中的flushCache属性默认为“false”，默认不清除
4. sqlSession.clearCache() 方法只清除一级缓存，因为是sqlSession调用的，只作用到这一级
5. localCacheScope：本地缓存的作用域，只影响一级缓存，在setting 标签中设置，取值可以是`SESSION | STATEMENT` 默认为`SESSION`，表示在session中有一级缓存，`STATEMENT`相当于禁止了session中的一级缓存

#### 缓存原理

- 多个sqlSession同时向数据库中查数据，查得的数据放在对应的sqlSession的一级缓存中
- 二级缓存以namespace为分隔，基本上就是不同的mapper，每次sqlSession关闭后，就把一级缓存中的数据放到二级缓存中去
- 新会话进入会先去查找二级缓存中是否有数据，然后查一级缓存，最后查数据库
- 要想自定义缓存，可以继承cache接口

#### Ehcache缓存

是一个java的快速缓存框架

使用ehcache可以自己实现cache接口自定义，也可以在github上找专门的整合方式，根据说明自行配置

配置清单如下:

1. 在全局配置文件的setting标签下启用缓存

2. 在mapper的xml文件中添加

   ```xml
   <cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
   ```

3. 还可以配置具体参数

   ```xml
   <cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
       <property name="timeToIdleSeconds" value="3600"/><!--1 hour-->
       <property name="timeToLiveSeconds" value="3600"/><!--1 hour-->
       <property name="maxEntriesLocalHeap" value="1000"/>
       <property name="maxEntriesLocalDisk" value="10000000"/>
       <property name="memoryStoreEvictionPolicy" value="LRU"/>
     </cache>
   ```

4. 除了使用`<cache>`标签，还可以使用cache-ref指定和什么namespace使用相同的缓存

   `<cache-ref namespace="me.rsnomis.dao.EmployeeMapper"/>`

也可以在resource目录下添加一个ehcache.xml配置文件做缓存全局配置

#### mybati整合spring

