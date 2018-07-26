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
     * 测试where动态sql标签
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

    /**
     * 测试trim动态sql标签
     * @param e
     */
    public void testSelectEmpsByConditionTrim(Employee e) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            List<Employee> emps = mapper.getEmpsByConditionTrim(e);
            for(Employee item : emps) {
                System.out.println(item);
            }
        } finally {
            session.close();
        }
    }

    /**
     * 测试choose动态sql标签
     * @param e
     */
    public void testSelectEmpsByConditionChoose(Employee e) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            List<Employee> emps = mapper.getEmpsByConditionChoose(e);
            for(Employee item : emps) {
                System.out.println(item);
            }
        } finally {
            session.close();
        }
    }

    /**
     * 测试set动态sql标签
     * @param e
     */
    public void testUpdateEmpByIdSet(Employee e) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            mapper.updateEmpByIdSet(e);
            session.commit();
        } finally {
            session.close();
        }
    }

    /**
     * 测试foreach动态sql标签
     * @param list
     */
    public void testSelectEmpsByConditionForeach(List<Integer> list) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            List<Employee> emp = mapper.getEmpsByConditionForeach(list);
            System.out.println(emp);
        } finally {
            session.close();
        }
    }

    /**
     * 测试批量添加
     * @param list
     */
    public void testInsertEmps(List<Employee> list) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            mapper.addEmps(list);
            session.commit();
        } finally {
            session.close();
        }
    }

    /**
     * 测试内置变量_patameter和_databaseId
     * @param e
     */
    public void testSelectEmpsInnerPatameter(Employee e) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            List<Employee> emp = mapper.getEmpsInnerPatameter(e);
            System.out.println(emp);
        } finally {
            session.close();
        }
    }

    /**
     * 测试bind标签
     * @param name
     */
    public void testSelectEmpsByNameBind(String name) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            List<Employee> emp = mapper.getEmpsByNameBind(name);
            System.out.println(emp);
        } finally {
            session.close();
        }
    }

    /**
     * 测试sql标签
     * @param id
     */
    public void testSelectEmpByIdSQL(Integer id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee emp = mapper.getEmpByIdSQL(id);
            System.out.println(emp);
        } finally {
            session.close();
        }
    }
}