package me.rsnomis;

import me.rsnomis.test.GeneratorTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MyTest {

    private GeneratorTest generatorTest = null;

    @Before
    public void init() throws IOException{
        generatorTest = new GeneratorTest();
    }

    @Test
    public void testSelectAll() {
        generatorTest.testSelectAll();
    }

    @Test
    public void testSelectByExample() {
        generatorTest.testSelectByExample();
    }

}
