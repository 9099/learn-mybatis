package me.rsnomis;

import me.rsnomis.bean.EmpStatus;
import me.rsnomis.bean.Employee;
import me.rsnomis.test.MybatisTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyTest {
    private MybatisTest mybatisTest = null;

    @Before
    public void setSqlSessionFactory() throws IOException {
        this.mybatisTest = new MybatisTest();
    }

    @Test
    public void testInsert() {
        mybatisTest.testSelectEmps();
    }

    @Test
    public void testBatch() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee(null,"sniffer","sniffer@163.com","1"));
        list.add(new Employee(null,"smith","smith@163.com","1"));
        list.add(new Employee(null,"simone","simone@163.com","1"));
        list.add(new Employee(null,"sarry","sarry@163.com","1"));
        mybatisTest.testBatch(list);
    }

    @Test
    public void testEnum(){
        Employee e = new Employee(null, "jim","jim@163.com","1");
        e.setEmpStatus(EmpStatus.LOGOUT);
        mybatisTest.testEnum(e);
    }

    @Test
    public void testEnumUse() {
        mybatisTest.testEnumUse();
    }
}

