package com.example.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/4 17:25
 */

@Mapper
public interface UserDao extends BaseMapper<User> {
}
