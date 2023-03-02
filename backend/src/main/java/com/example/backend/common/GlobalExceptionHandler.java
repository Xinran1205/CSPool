package com.example.backend.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author Xinran
 * @version 1.0
 * @description 全局异常捕获类
 * @date 2023/3/2 14:50
 */
//这个注解就像AOP中的通知，意思是：加了后面注解的类遇到异常都会走到这
//AOP思想
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    //处理sql异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public RestResult<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        if(ex.getMessage().contains("Duplicate entry")){
            //这里我们根据msg数据的空格分开数据装成数组，拿第三个数据就是用户名
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return RestResult.error(msg,0);
        }

        return RestResult.error("未知错误",0);
    }
}
