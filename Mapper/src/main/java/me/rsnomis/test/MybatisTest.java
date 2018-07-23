package me.rsnomis.test;

import me.rsnomis.bean.Employee;
import me.rsnomis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 删除数据
     * @param id
     */
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

    /**
     * 更新数据
     * @param e
     */
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

    /**
     * 传入多个数据
     * @param id
     * @param last_name
     */
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

    /**
     * 传入map
     * @param id
     * @param lastName
     */
    public void testSelectEmpByMap(Integer id, String lastName) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Map<String, Object> map = new HashMap();
            map.put("id", id);
            map.put("lastName", lastName);
            Employee e = mapper.getEmpByMap(map);
            System.out.println(e);
        } finally {
            session.close();
        }
    }

    /**
     * 传入参数是map
     * @param e
     */
    public void testInsertEmpByPojo(Employee e) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            mapper.insertEmpByPojo(e);
            session.commit();
        } finally {
            session.close();
        }
    }

    /**
     * 返回列表
     * @param lastName
     */
    public void testSelectEmpByLastName(String lastName) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            List<Employee> emp = mapper.getEmpByLastName(lastName);
            for (Employee e : emp) {
                System.out.println(e);
            }
        } finally {
            session.close();
        }
    }

    /**
     * 若接口中返回类型为map，映射文件的resultType直接写map
     * 则将结果封装为一个map返回，map的key是列名，value是值
     * @param id
     */
    public void testSelectEmpByIdReturnMap(Integer id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Map<String, Object> map = mapper.getEmpByIdReturnMap(1);
            System.out.println(map);
        } finally {
            session.close();
        }
    }

    /**
     * 返回多条记录封装为一个map
     * map的key需要在接口文件中使用@MapKey注解定义
     * map的alue就是查询的值
     * resultType写value的全类名
     *
     * @param lastName
     */
    public void testSelectEmpByLastNameReturnMap(String lastName) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Map<Integer, Employee> map = mapper.getEmpByLastNameReturnMap(lastName);
            for (Map.Entry<Integer, Employee> e : map.entrySet()) {
                System.out.println(e.getKey() + e.getValue().toString());
            }
        } finally {
            session.close();
        }
    }
}