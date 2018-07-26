package me.rsnomis;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import me.rsnomis.bean.Employee;
import me.rsnomis.test.MybatisTest;

public class MyTest {
    private MybatisTest mybatisTest = null;

    @Before
    public void setSqlSessionFactory() throws IOException {
        this.mybatisTest = new MybatisTest();
    }

    @Test
    public void testFirstLevelCache() {
        mybatisTest.testFirstLevelCache(1);
    }

    @Test
    public void testDifSession() {
        mybatisTest.testDifSession(1);
    }

    @Test
    public void testDifQuery() {
        mybatisTest.testDifQuery(1,2);
    }

    @Test
    public void testSelectAfterInsert() {
        mybatisTest.testSelectAfterInsert(1, new Employee(null, "Bin", "Bin@163.com", "1", null));
    }

    @Test
    public void testClearCache() {
        mybatisTest.testClearCache(1);
    }

    @Test
    public void testSecondCache() {
        mybatisTest.testSecondCahce(1);
    }

    @Test
    public void testSelectEmpUseCache() {
        mybatisTest.testSelectEmpUseCache(1);
    }

    @Test
    public void testSecondCacheAfterInsert() {
        mybatisTest.testSecondCacheAfterInsert(1,new Employee(null,"jim", "jim@163.com", "0", null));
    }
}

