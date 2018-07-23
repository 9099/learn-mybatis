package me.rsnomis.dao;

import me.rsnomis.bean.Employee;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {

    Employee getEmpById(Integer id);

    Employee getEmpByIdAndLastName(Integer id, String last_name);

    /**
     * mybatis允许Integer Long Boolean等返回值，返回操作信息，直接在函数前修改返回值类型即可
     * @param employee
     */
    Boolean addEmp(Employee employee);

    void updateEmp(Employee employee);

    void deleteEmpById(Integer id);

    Employee getEmpByMap(Map map);

    void insertEmpByPojo(Employee e);

    List<Employee> getEmpByLastName(String lastName);

    Map<String, Object> getEmpByIdReturnMap(Integer id);

    @MapKey("id")
    Map<Integer, Employee> getEmpByLastNameReturnMap(String lastName);
}
