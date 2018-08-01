package me.rsnomis.plugin;

import me.rsnomis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisPlugin {

    private SqlSessionFactory sqlSessionFactory = null;

    public MybatisPlugin() throws IOException {
        String source = "mybatis-config.xml";
        InputStream in = Resources.getResourceAsStream(source);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
    }
    /**
     * 编写插件
     * 1.编写Interceptor的实现类
     * 2.使用@Intercepts注解编写插件签名，即指定要拦截什么对象的什么方法
     * 3.把插件注册到全局配置文件中,把插件注册到全局配置文件中，用plugin 标签填写上插件的全类名
     */
    public void testPlugin() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            mapper.getEmpById(1);
        } finally {
            session.close();
        }
    }
}
