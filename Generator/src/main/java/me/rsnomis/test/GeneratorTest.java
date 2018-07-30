package me.rsnomis.test;

import me.rsnomis.bean.Employee;
import me.rsnomis.bean.EmployeeExample;
import me.rsnomis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GeneratorTest {

    private SqlSessionFactory sqlSessionFactory= null;

    public GeneratorTest() throws IOException {
        InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
    }

    /**
     * 测试查询所有数据
     * 本方法只有targetRuntime用mybatis3Simple中才有
     */
    /*public void testSelectAll() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            List<Employee> list =mapper.selectAll();
            for(Employee e : list) {
                System.out.println(e.getId());
            }
        }finally {
            session.close();
        }
    }*/

    /**
     * 测试查询所有数据
     * 本方法只有targetRuntime用mybatis3中才有
     * 这里不传参数就是查询所有
     */
    public void testSelectAll() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            List<Employee> list =mapper.selectByExample(null);
            for(Employee e : list) {
                System.out.println(e.getId());
            }
        }finally {
            session.close();
        }
    }

    /**
     * 带参数查询
     */
    public void testSelectByExample() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            /*查询名字中带有o并且性别是1的
            若要使用or,则再添加一个新的criteria，然后用example.()添加进去就行*/
            EmployeeExample example = new EmployeeExample();
            EmployeeExample.Criteria criteria = example.createCriteria();
            criteria.andLastNameLike("%o%");
            criteria.andGenderEqualTo("1");
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            List<Employee> list =mapper.selectByExample(example);
            for(Employee e : list) {
                System.out.println(e.getId());
            }
        }finally {
            session.close();
        }
    }

}
