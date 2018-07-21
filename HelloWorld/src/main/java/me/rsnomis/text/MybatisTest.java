package me.rsnomis.text;

import me.rsnomis.bean.Employee;
import me.rsnomis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

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
    public void selectOne() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession openSession = sqlSessionFactory.openSession();

        Employee e = (Employee)openSession.selectOne("me.snomis.helloworld.dao.EmployeeMapper.selectEmp", 1);
        System.out.println(e);
        openSession.close();
    }

    private SqlSessionFactory getSqlSessionFactory() throws IOException{
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    public void getEmpById() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        // mapper是一个代理对象
        EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
        Employee e = mapper.getEmpById(1);
        System.out.println(e);
        openSession.close();
    }
}