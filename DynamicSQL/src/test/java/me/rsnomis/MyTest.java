package me.rsnomis;

import me.rsnomis.bean.Department;
import me.rsnomis.bean.Employee;
import me.rsnomis.test.MybatisTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyTest {
    private MybatisTest mybatisTest = null;

    @Before
    public void setSqlSessionFactory() throws IOException {
        this.mybatisTest = new MybatisTest();
    }

    @Test
    public void getEmpsByConditionIf() {
        Employee e = new Employee(1, null, null, null, null);
        mybatisTest.testSelectEmpsByConditionIf(e);
    }

    @Test
    public void getEmpsByConditionWhere() {
        Employee e = new Employee(null, "Tom", null, null, null);
        mybatisTest.testSelectEmpsByConditionWhere(e);

    }

    @Test
    public void getEmpsByConditionTrim() {
        Employee e = new Employee(null, "Tom", null, null, null);
        mybatisTest.testSelectEmpsByConditionTrim(e);
    }

    @Test
    public void getEmpsByConditionChoose() {
        Employee e = new Employee(null, "Tom", null, null, null);
        Employee e1 = new Employee(null, null, null, null, null);
        Employee e2 = new Employee(1, null, null, null, null);
        mybatisTest.testSelectEmpsByConditionChoose(e);
        mybatisTest.testSelectEmpsByConditionChoose(e1);
        mybatisTest.testSelectEmpsByConditionChoose(e2);
    }

    @Test
    public void testUpdateEmpByIdSet() {
        Employee e = new Employee(2, "Jenna", "Jenna@163.com", "0", null);
        mybatisTest.testUpdateEmpByIdSet(e);
    }

    @Test
    public void testSelectEmpsByConditionForeach() {
        mybatisTest.testSelectEmpsByConditionForeach(Arrays.asList(1,2,3,4));
    }

    @Test
    public void testInsertEmps() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee(null,"june","june@163.com","0",new Department(1,null)));
        list.add(new Employee(null,"fuck","fuck@163.com","0",new Department(1,null)));
        mybatisTest.testInsertEmps(list);
    }

    @Test
    public void testSelectEmpsInnerPatameter() {
        mybatisTest.testSelectEmpsInnerPatameter(new Employee(1,null,null,null,null));
    }

    @Test
    public void testSelectEmpsByNameBind() {
        mybatisTest.testSelectEmpsByNameBind("T");
    }

    @Test
    public void testSelectEmpByIdSQL() {
        mybatisTest.testSelectEmpByIdSQL(1);
    }
}

