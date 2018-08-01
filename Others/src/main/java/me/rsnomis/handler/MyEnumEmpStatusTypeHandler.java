package me.rsnomis.handler;


import me.rsnomis.bean.EmpStatus;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 实现TypeHandler接口或者继承BaseTypeHandler
 * 写完后在全局配置文件中配置
 */
public class MyEnumEmpStatusTypeHandler implements TypeHandler<EmpStatus>{
    /**
     * 定义当前数据怎样保存到数据库中
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, EmpStatus parameter, JdbcType jdbcType) throws SQLException {
        System.out.println(parameter.getCode().toString());
        ps.setString(i,parameter.getCode().toString());
    }

    /**
     * 获取值
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public EmpStatus getResult(ResultSet rs, String columnName) throws SQLException {
        int code = rs.getInt(columnName);

        //需要根据从数据库中拿到的枚举状态码返回一个枚举对象
        System.out.println(code);
        return EmpStatus.getEmpStatusByCode(code);
    }

    @Override
    public EmpStatus getResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);

        //需要根据从数据库中拿到的枚举状态码返回一个枚举对象
        System.out.println(code);
        return EmpStatus.getEmpStatusByCode(code);
    }

    @Override
    public EmpStatus getResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);

        //需要根据从数据库中拿到的枚举状态码返回一个枚举对象
        System.out.println(code);
        return EmpStatus.getEmpStatusByCode(code);
    }
}
