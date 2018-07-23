package me.rsnomis.dao;

import me.rsnomis.bean.Employee;

import java.util.List;

public interface EmployeeMapperPlus {

    Employee getEmpById(Integer id);

    Employee getEmpAndDept(Integer id);

    Employee getEmpAndDeptDif(Integer id);

    Employee getEmpByIdStep(Integer id);

    Employee getEmpByIdLazy(Integer id);

    List<Employee> getEmpByDeptId(Integer id);

    Employee getEmpByIdDis(Integer id);
}
