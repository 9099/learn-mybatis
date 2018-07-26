package me.rsnomis.dao;

import me.rsnomis.bean.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeMapper {

    List<Employee> getEmpsByConditionIf(Employee e);

    List<Employee> getEmpsByConditionWhere(Employee e);

    List<Employee> getEmpsByConditionTrim(Employee e);

    List<Employee> getEmpsByConditionChoose(Employee e);

    void updateEmpByIdSet(Employee e);

    List<Employee> getEmpsByConditionForeach(@Param("ids") List<Integer> list);

    void addEmps(@Param("emps") List<Employee> emps);

    List<Employee> getEmpsInnerPatameter(Employee e);

    List<Employee> getEmpsByNameBind(@Param("name") String name);

    Employee getEmpByIdSQL(Integer id);

}
