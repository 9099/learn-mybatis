package me.rsnomis.test;


import me.rsnomis.bean.Department;
import me.rsnomis.bean.Employee;
import me.rsnomis.dao.DepartmentMapper;
import me.rsnomis.dao.EmployeeMapper;
import me.rsnomis.dao.EmployeeMapperPlus;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;


public class MybatisTestPlus {

    private SqlSessionFactory sqlSessionFactory = null;

    public MybatisTestPlus() throws IOException{
        String source = "mybatis-config.xml";
        InputStream in = Resources.getResourceAsStream(source);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
    }

    /**
     * resultMap 封装指定映射
     * @param id
     */
    public void testSelectById(Integer id){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapperPlus mapper = session.getMapper(EmployeeMapperPlus.class);
            Employee e = mapper.getEmpById(id);
            System.out.println(e);
        } finally {
            session.close();
        }
    }

    /**
     * 级联查询，在Employee的bean中添加department属性
     * @param id
     */
    public void testSelectEmpAndDept(Integer id){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapperPlus mapper = session.getMapper(EmployeeMapperPlus.class);
            Employee e = mapper.getEmpAndDept(id);
            System.out.println(e);
            System.out.println(e.getDept().toString());
        } finally {
            session.close();
        }
    }

    /**
     * 使用association测试级联属性的查找
     * @param i
     */
    public void testSelectEmpAndDeptDif(int i) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapperPlus mapper = session.getMapper(EmployeeMapperPlus.class);
            Employee e = mapper.getEmpAndDeptDif(i);
            System.out.println(e);
            System.out.println(e.getDept().toString());
        } finally {
            session.close();
        }
    }

    /**
     * 用association进行分步查询
     * @param i
     */
    public void testSelectEmpByIdStep(int i) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapperPlus mapper = session.getMapper(EmployeeMapperPlus.class);
            Employee e = mapper.getEmpByIdStep(i);
            System.out.println(e);
            System.out.println(e.getDept().toString());
        } finally {
            session.close();
        }
    }

    /**
     * 延迟加载,没有用部门信息的时候只发第一条查询语句，打印部门信息后，会发送两条语句
     * @param i
     */
    public void testSelectEmpByIdLazy(int i) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapperPlus mapper = session.getMapper(EmployeeMapperPlus.class);
            Employee e = mapper.getEmpByIdLazy(i);
            System.out.println(e.getEmail());
            //System.out.println(e.getDept().toString());
        } finally {
            session.close();
        }
    }

    /**
     * 一对多查询，使用collection查询同一部门的所有员工
     * @param i
     */
    public void testSelectDeptByIdPlus(int i) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            DepartmentMapper mapper = session.getMapper(DepartmentMapper.class);
            Department e = mapper.getDeptByIdPlus(i);
            System.out.println(e);
            for(Employee emp : e.getEmps()) {
                System.out.println(emp);
            }
        } finally {
            session.close();
        }
    }

    /**
     * collection也有分段查询，同样支持延迟加载
     * 去掉下面注释将发送两次查询语句，加上注释则只查询一次
     * @param i
     */
    public void testSelectDeptByIdStep(int i) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            DepartmentMapper mapper = session.getMapper(DepartmentMapper.class);
            Department e = mapper.getDeptByIdStep(i);
            System.out.println(e.getId());
            /*
            for(Employee emp : e.getEmps()) {
                System.out.println(emp);
            }
            */
        } finally {
            session.close();
        }
    }

    /**
     * 鉴别器的使用
     * @param i
     */
    public void testSelectEmpByIdDis(int i) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapperPlus mapper = session.getMapper(EmployeeMapperPlus.class);
            Employee e = mapper.getEmpByIdDis(i);
            System.out.println(e);
            System.out.println(e.getDept());
        } finally {
            session.close();
        }
    }
}