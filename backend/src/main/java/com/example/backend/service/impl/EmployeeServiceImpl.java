package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.dao.EmployeeDao;
import com.example.backend.domain.Employee;
import com.example.backend.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/2/26 16:34
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements EmployeeService {
}
