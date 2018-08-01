package me.rsnomis.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.Properties;

/**
 * 完成插件签名：
 * 用Intercepts注解定义拦截什么对象的什么方法
 * 注解中包含一个Signature数组，每个Signature中
 * type是拦截什么对象，四大对象之一
 * method拦截对象中的哪个方法
 * args指定方法的参数列表
 *
 */
@Intercepts({
        @Signature(type= StatementHandler.class,method="parameterize",args=java.sql.Statement.class)
})
public class MyFirstPlugin implements Interceptor{
    /**
     * intercept 拦截
     * 拦截目标对象的目标方法的执行
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        /**
         * 执行目标方法，获得返回值
         * 在proceed方法前后添加其他方法就实现了附加功能
         * 这里实现一个功能，查询1号员工，返回4号员工
         */
        System.out.println("MyFirstPlugin...intercept:" + invocation.getMethod());

        //拿到StatementHandler => parameterHandler=>parameterObject的值
        //拿到目标对象
        Object target = invocation.getTarget();
        //拿到对象的原数据
        MetaObject metaObject = SystemMetaObject.forObject(target);
        //拿到StatementHandler => parameterHandler=>parameterObject的值
        Object value = metaObject.getValue("parameterHandler.parameterObject");
        System.out.println(value);
        //设置parameterObject的值
        metaObject.setValue("parameterHandler.parameterObject", 4);
        Object proceed = invocation.proceed();

        //返回返回值
        return proceed;
    }

    /**
     * plugin 插件
     * 用来包装目标对象，包装就是创建一个代理对象
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        /**
         * mybatis为了方便，提供了一个Plugin类，wrap方法传入目标对象和拦截器
         * 这里使用当前拦截器，拦截和包装目标对象
         */
        System.out.println("MyFirstPlugin...intercept:包装的对象" + target);
        Object wrap = Plugin.wrap(target, this);

        //返回为当前target创建的动态代理
        return wrap;
    }

    /**
     * setProperties
     * 将插件注册时的property属性设置进来
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("插件的配置信息" + properties);
    }
}
