package me.rsnomis.dao;

import me.rsnomis.bean.Department;

public interface DepartmentMapper {

    Department getDeptById(Integer id);

    Department getDeptByIdPlus(Integer id);

    Department getDeptByIdStep(Integer id);
}
