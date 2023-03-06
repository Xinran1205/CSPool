package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.dao.UserDao;
import com.example.backend.domain.User;
import com.example.backend.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/4 17:23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
}
