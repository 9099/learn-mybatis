package me.rsnomis.dao;


import me.rsnomis.bean.Employee;

public interface EmployeeMapper {


    Employee getEmpById(Integer id);

    void addEmp(Employee e);

    Employee getEmpUseCache(Integer id);
}
