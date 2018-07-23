package me.rsnomis.dao;

import me.rsnomis.bean.Employee;

import java.util.List;

public interface EmployeeMapper {

    List<Employee> getEmpsByConditionIf(Employee e);
    List<Employee> getEmpsByConditionWhere(Employee e);

}
