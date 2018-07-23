package me.rsnomis.test;


import me.rsnomis.bean.Employee;
import me.rsnomis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisTest {

    private SqlSessionFactory sqlSessionFactory = null;

    public MybatisTest() throws IOException{
        String source = "mybatis-config.xml";
        InputStream in = Resources.getResourceAsStream(source);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
    }

    /**
     * 测试if动态sql标签
     * @param e
     */
    public void testSelectEmpsByConditionIf(Employee e) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            List<Employee> emps = mapper.getEmpsByConditionIf(e);
            for(Employee item : emps) {
                System.out.println(item);
            }
        } finally {
            session.close();
        }
    }

    /**
     * 测试if动态sql标签
     * @param e
     */
    public void testSelectEmpsByConditionWhere(Employee e) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            List<Employee> emps = mapper.getEmpsByConditionWhere(e);
            for(Employee item : emps) {
                System.out.println(item);
            }
        } finally {
            session.close();
        }
    }
}