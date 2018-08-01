package me.rsnomis;

import me.rsnomis.plugin.MybatisPlugin;
import org.junit.Before;
import org.junit.Test;

public class MybatisPluginTest {

    private MybatisPlugin mybatisPlugin= null;

    @Before
    public void setMybatisPlugin() throws Exception{
        mybatisPlugin = new MybatisPlugin();
    }

    @Test
    public void testPlugin() {
        mybatisPlugin.testPlugin();
    }
}
