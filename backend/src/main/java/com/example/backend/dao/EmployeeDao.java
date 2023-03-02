package com.example.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.domain.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/2/26 16:27
 */

@Mapper
public interface EmployeeDao extends BaseMapper<Employee> {
}
