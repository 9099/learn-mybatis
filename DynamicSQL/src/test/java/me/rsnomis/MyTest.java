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
    public void getEmpsByConditionIf() {
        Employee e = new Employee(1, null, null, null, null);
        mybatisTest.testSelectEmpsByConditionIf(e);
    }

    @Test
    public void getEmpsByConditionWhere() {
        Employee e = new Employee(null, "Tom", null, null, null);
        mybatisTest.testSelectEmpsByConditionWhere(e);
    }
}

