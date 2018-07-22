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

单个参数：在接口函数中，若只写了一个参数，那么Mybatis不会做特殊处理，参数名可以任意取

​	#{参数名} 参数名随便写，因为只有一个参数

多个参数：在接口函数中，若写了多个参数，会做特殊处理，多个参数会被封装为一个map，#{}则是在map中取值，map的构成是：

key: param1 ... paramN

value: 传入的参数值

可以使用：

​	#{arg0}, #{arg1} ...

​	#{param1}, #{param2}...

​	旧版本可以使用: #{0}, #{1}

可以命名参数：

明确指定封装为map后的key值，方法是在接口中使用@Param("id")参数，这样就可以直接在xml映射文件中使用#{变量名}这样的方式了

```java
package me.rsnomis.dao;

import me.rsnomis.bean.Employee;

public interface EmployeeMapper {

    Employee getEmpByIdAndLastName(@Param("id")Integer id, @Param("last_name")String last_name);
    
}
```

