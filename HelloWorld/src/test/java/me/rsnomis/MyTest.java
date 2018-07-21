package me.rsnomis;

import me.snomis.helloworld.test.MybatisTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MyTest {
    private MybatisTest mybatisTest;

    @Before
    public void setMybatisTest() {
        mybatisTest = new MybatisTest();
    }

    @Test
    public void test1() throws IOException {
        mybatisTest.selectOne();
    }

    @Test
    public void test2() throws IOException {
        mybatisTest.getEmpById();
    }
}
