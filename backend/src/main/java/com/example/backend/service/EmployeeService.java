package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.domain.Employee;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/2/26 16:33
 */
@Transactional
public interface EmployeeService extends IService<Employee> {
}
