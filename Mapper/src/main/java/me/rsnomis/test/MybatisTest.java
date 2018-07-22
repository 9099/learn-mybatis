package me.rsnomis.test;

import me.rsnomis.bean.Employee;
import me.rsnomis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisTest {

    private SqlSessionFactory sqlSessionFactory = null;

    public MybatisTest() throws IOException{
        String source = "mybatis-config.xml";
        InputStream in = Resources.getResourceAsStream(source);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
    }

    /**
     * 1、mybatis允许增删改定义返回类型，直接在接口的函数定义上定义就行
     * 返回类型可以是Boolean、Long、Integer、void
     * 2、增删改需要手动提交 session.commit()
     * 3、可以在openSession时指定是否自动提交
     * sqlSessionFactory.openSession(true)
     * @param e
     */
    public void testInsert(Employee e) {
        SqlSession session = sqlSessionFactory.openSession();
        //如下自动提交
        //SqlSession session = sqlSessionFactory.openSession(true);
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Boolean result = mapper.addEmp(e);
            System.out.println(result);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void testDelete(Integer id) {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper= session.getMapper(EmployeeMapper.class);
            mapper.deleteEmpById(id);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void testUpdate(Employee e) {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            mapper.updateEmp(e);
            session.commit();
        } finally {
            session.close();
        }
    }

    public void testSelectByIdAndLastName(Integer id, String last_name) {
        SqlSession session = sqlSessionFactory.openSession();
        //如下自动提交
        //SqlSession session = sqlSessionFactory.openSession(true);
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee e = mapper.getEmpByIdAndLastName(id, last_name);
            System.out.println(e);
        } finally {
            session.close();
        }
    }
}