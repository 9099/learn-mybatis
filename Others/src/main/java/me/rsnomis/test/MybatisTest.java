package me.rsnomis.test;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import me.rsnomis.bean.EmpStatus;
import me.rsnomis.bean.Employee;
import me.rsnomis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
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
     * 分页查员工
     * @return
     */
    public void testSelectEmps() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Page<Object> page = PageHelper.startPage(1, 1);
            List<Employee> emps = mapper.getEmps();
            PageInfo<Employee> employeePageInfo = new PageInfo<>(emps,4);
            System.out.println(employeePageInfo.getNavigatePages());
            for (int i =0; i<employeePageInfo.getNavigatePages(); ++i) {
                System.out.println(i);
            }
        } finally {
            session.close();
        }
    }

    /**
     * 测试使用batch Executor进行批量存储数据
     * 在创建Executor的时候指定创建的Executor类别
     * 批量操作只预编译sql语句一次，然后只要每次在添加记录的时候设置参数就好了，不用再预编译语句
     * 用simple的Executor则每次添加数据都要预编译一次sql语句
     * @param e
     */
    public void testBatch(List<Employee> e) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            for (Employee emp : e) {
                mapper.addEmp(emp);
            }
            session.commit();
        }finally {
            session.close();
        }
    }

    /**
     * oracle分页：
     *  oracle的分页借助rownum：行号，oracle数据库中每条记录有个行号
     *  但是按照行号查出数据后行号可能变化，查询需要一个子查询
     *  比如查询 1<=rownum<=5的行，不能一次查出，需要分两步，先查rownum<=5
     *  然后在查得的结果中执行rownum>=1的查询
     *  存储过程需要事先保存到数据库系统中

     create or replace procedure
     hello_test(
     p_start in int, p_end in int, p_count out int, p_emps out sys_refcursor
     ) as
     begin
     select count(*) into p_count from employees;
     open p_emps for
     select * from (select rownum rn, e.* from employees e where rownum <= p_end)
     where rn>= p_start;
     end hello_test;

     *
     */
    public void testProcedure() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            me.rsnomis.bean.Page page = new me.rsnomis.bean.Page();
            page.setStart(1);
            page.setEnd(5);
            mapper.getPageByProcedure(page);
            System.out.println(page.getCount());
            System.out.println(page.getEmps().size());
        } finally {
            session.close();
        }
    }

    /**
     * 枚举类的测试
     */
    public void testEnumUse() {
        EmpStatus login = EmpStatus.LOGIN;
        System.out.println(login.ordinal());
        System.out.println(login.name());
        System.out.println(login.getCode());
        System.out.println(login.getMsg());
    }

    /**
     * 处理枚举类型
     * 数据库添加一列 ALTER TABLE tbl_employee ADD empStatus VARCHAR(11)
     * mybatis保存枚举对象时默认是使用枚举对象的名字
     * 要改变使用的typehander时可以在全局配置文件中的setting的typeHandlers中设置
     * 或者在mapper文件中指定，要注意存入和取出操作使用相同的typeHandler
     * 如果需要数据库保存EmpStatus中的状态吗
     */
    public void testEnum(Employee e) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            mapper.addEmp(e);
            Integer id = e.getId();
            System.out.println(id);
            Employee emp = mapper.getEmpById(id);
            System.out.println(emp.getEmpStatus());
            session.commit();
        } finally {
            session.close();
        }
    }
}