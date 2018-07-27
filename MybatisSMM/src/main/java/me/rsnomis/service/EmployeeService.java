package me.rsnomis.service;

import me.rsnomis.bean.Employee;
import me.rsnomis.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    public Employee getEmpById(Integer id) {
        return employeeMapper.getEmpById(id);
    }
}
