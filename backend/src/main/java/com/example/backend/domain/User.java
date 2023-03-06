package com.example.backend.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/4 17:17
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //账号名
    private String username;


    //手机号，我们项目先用手机号登录，后面有空改为github或者其他方式登录
    private String phone;


    //性别 0 女 1 男
    private String sex;


    //头像
    private String avatar;


    //状态 0:禁用，1:正常
    private Integer status;
}
