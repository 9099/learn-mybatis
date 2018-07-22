package me.rsnomis;

import me.rsnomis.bean.Employee;
import me.rsnomis.test.MybatisTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MyTest {
    private MybatisTest mybatisTest = null;

    @Before
    public void setSqlSessionFactory() throws IOException {
        this.mybatisTest = new MybatisTest();
    }

    @Test
    public void testInsert() {
        Employee employee = new Employee();
        employee.setEmail("God@163.com");
        employee.setGender("1");
        employee.setLastName("God");
        mybatisTest.testInsert(employee);
    }

    @Test
    public void testGetId() {
        Employee employee = new Employee();
        employee.setEmail("God@163.com");
        employee.setGender("1");
        employee.setLastName("God");
        System.out.println("id=" + employee.getId());
        mybatisTest.testInsert(employee);
        System.out.println("id=" + employee.getId());
    }

    @Test
    public void testDelete() {
        mybatisTest.testDelete(2);
    }

    @Test
    public void testUpdate() {
        Employee employee = new Employee();
        employee.setEmail("Marry@163.com");
        employee.setGender("1");
        employee.setLastName("Marry");
        employee.setId(1);
        mybatisTest.testUpdate(employee);
    }

    @Test
    public void testselectByIdAndLastName() {
        mybatisTest.testSelectByIdAndLastName(1,"Marry");
    }
}

