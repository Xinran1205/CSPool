package com.example.backend.common;

/**
 * @author Xinran
 * @version 1.0
 * @description 自定义业务异常类
 * @date 2023/3/3 16:16
 */

//自定义抛异常可以改里面的message，但是具体处理这个异常，我们是在全局的异常处理类中处理
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
