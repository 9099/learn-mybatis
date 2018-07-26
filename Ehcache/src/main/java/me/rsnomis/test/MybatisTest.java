package me.rsnomis.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import me.rsnomis.bean.Employee;
import me.rsnomis.dao.EmployeeMapper;

public class MybatisTest {

    private SqlSessionFactory sqlSessionFactory = null;

    public MybatisTest() throws IOException{
        String source = "mybatis-config.xml";
        InputStream in = Resources.getResourceAsStream(source);
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
    }

    /**
     * 测试一级缓存
     * @param id
     */
    public void testFirstLevelCache(Integer id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee emp = mapper.getEmpById(id);
            Employee emp1 = mapper.getEmpById(id);
            System.out.println(emp);
            //emp1是从缓存中读取数据，所以只发了一次数据库查询，两个对象相等
            System.out.println(emp1 == emp);
        } finally {
            session.close();
        }
    }

    /**
     * 不同SqlSession会话，一级缓存不共享，会发送两次查询请求
     * @param id
     */
    public void testDifSession(Integer id) {
        SqlSession session1 = sqlSessionFactory.openSession();
        SqlSession session2 = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper1 = session1.getMapper(EmployeeMapper.class);
            Employee emp1 = mapper1.getEmpById(id);
            System.out.println(emp1);

            EmployeeMapper mapper2 = session2.getMapper(EmployeeMapper.class);
            Employee emp2 = mapper2.getEmpById(id);
            System.out.println(emp2);
            //emp1是从缓存中读取数据，所以只发了一次数据库查询，两个对象相等
            System.out.println(emp1 == emp2);
        } finally {
            session1.close();
            session2.close();
        }
    }

    /**
     * 相同SqlSession会话，查询条件不同，没有缓存，会发送两次查询请求
     * @param id1
     * @param id2
     */
    public void testDifQuery(Integer id1, Integer id2) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee emp1 = mapper.getEmpById(id1);
            System.out.println(emp1);

            Employee emp2 = mapper.getEmpById(id2);
            System.out.println(emp2);
            //emp1是从缓存中读取数据，所以只发了一次数据库查询，两个对象相等
            System.out.println(emp1 == emp2);
        } finally {
            session.close();
        }
    }

    /**
     * 两次查询之间进行了增删改操作,一级缓存失效，发送两次请求
     * 不论SqlSession是否commit(),一级缓存都失效
     * @param id
     * @param e
     */
    public void testSelectAfterInsert(Integer id, Employee e) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee emp = mapper.getEmpById(id);
            System.out.println(emp);

            mapper.addEmp(e);
            //session.commit();

            Employee emp2 = mapper.getEmpById(id);
            System.out.println(emp2);
            System.out.println(emp == emp2);

        } finally {
            session.close();
        }
    }

    /**
     * 手动清了一级缓存，那么就会发两次请求了
     * @param id
     */
    public void testClearCache(Integer id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee emp = mapper.getEmpById(id);

            session.clearCache();

            Employee emp1 = mapper.getEmpById(id);
            System.out.println(emp1 == emp);
        } finally {
            session.close();
        }
    }

    /**
     * 测试二级缓存,这里注意要先关闭一个session才有效，因为一级缓存是在session关闭时才放到二级缓存中的
     * readOnly为fasle 返回的是另一个对象，true返回的是同一个对象
     * @param id
     */
    public void testSecondCahce(Integer id) {
        SqlSession session1 = sqlSessionFactory.openSession();
        SqlSession session2 = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper1 = session1.getMapper(EmployeeMapper.class);
            EmployeeMapper mapper2 = session2.getMapper(EmployeeMapper.class);
            Employee emp1 = mapper1.getEmpById(id);
            session1.close();
            Employee emp2 = mapper2.getEmpById(id);
            System.out.println(emp1 == emp2);
        } finally {
            session2.close();
        }
    }

    /**
     * select 标签属性添加useCache
     * @param id
     */
    public void testSelectEmpUseCache(Integer id) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee e = mapper.getEmpUseCache(id);
            System.out.println(e);
            Employee e1 = mapper.getEmpUseCache(id);
            System.out.println(e1);
        } finally {
            session.close();
        }
    }

    /**
     * 测试增删改是否清楚二级缓存
     * @param id
     * @param e
     */
    public void testSecondCacheAfterInsert(Integer id, Employee e){
        SqlSession session1 = sqlSessionFactory.openSession();
        SqlSession session2 = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper1 = session1.getMapper(EmployeeMapper.class);
            EmployeeMapper mapper2 = session2.getMapper(EmployeeMapper.class);

            Employee emp1 = mapper1.getEmpById(id);
            session1.close();

            mapper2.addEmp(e);
            
            Employee emp2 = mapper2.getEmpById(id);

            System.out.println(emp1);
            System.out.println(emp2);
        } finally {
            session2.close();
        }
    }
}