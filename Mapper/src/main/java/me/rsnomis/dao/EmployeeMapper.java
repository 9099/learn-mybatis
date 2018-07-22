package me.rsnomis.dao;

import me.rsnomis.bean.Employee;

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
}
