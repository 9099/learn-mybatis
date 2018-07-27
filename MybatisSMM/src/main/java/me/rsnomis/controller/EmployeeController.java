package me.rsnomis.controller;

import me.rsnomis.bean.Employee;
import me.rsnomis.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/emps")
    public String emps(Map<String, Object> map) {
        Employee emp = employeeService.getEmpById(1);
        map.put("emp", emp);
        return "list";
    }
}
