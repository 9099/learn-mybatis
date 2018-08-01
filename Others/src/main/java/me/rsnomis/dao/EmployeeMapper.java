package me.rsnomis.dao;

import me.rsnomis.bean.Employee;
import me.rsnomis.bean.Page;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {

    List<Employee> getEmps();

    void addEmp(Employee e);

    void getPageByProcedure(Page page);

    Employee getEmpById(Integer id);
}
