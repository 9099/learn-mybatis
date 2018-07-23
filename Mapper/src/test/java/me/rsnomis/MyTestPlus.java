package me.rsnomis;

import me.rsnomis.bean.Employee;
import me.rsnomis.test.MybatisTest;
import me.rsnomis.test.MybatisTestPlus;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MyTestPlus {
    private MybatisTestPlus mybatisTestPlus = null;

    @Before
    public void setSqlSessionFactory() throws IOException {
        this.mybatisTestPlus = new MybatisTestPlus();
    }

    @Test
    public void testSelectEmpById() {
        mybatisTestPlus.testSelectById(1);
    }

    @Test
    public void testSelectEmpAndDept() {
        mybatisTestPlus.testSelectEmpAndDept(1);
    }

    @Test
    public void testSelectEmpAndDeptDif() {
        mybatisTestPlus.testSelectEmpAndDeptDif(1);
    }

    @Test
    public void testSelectEmpByIdStep() {
        mybatisTestPlus.testSelectEmpByIdStep(1);
    }

    @Test
    public void testSelectEmpByIdLazy() {
        mybatisTestPlus.testSelectEmpByIdLazy(1);
    }

    @Test
    public void testSelectDeptByIdPlus() {
        mybatisTestPlus.testSelectDeptByIdPlus(1);
    }

    @Test
    public void testSelectDeptByIdStep() {
        mybatisTestPlus.testSelectDeptByIdStep(1);
    }

    @Test
    public void testSelectEmpByIdDis() {
        mybatisTestPlus.testSelectEmpByIdDis(2);
    }

}

