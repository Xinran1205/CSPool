package com.example.backend.common;

/**
 * @author Xinran
 * @version 1.0
 * @description 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * @date 2023/3/3 14:45
 */
//根据一次请求一个线程的原理（我们需要通过几个操作的线程id验证一下，线程id一样说明是一个线程），我们就可以通过线程获得到session的id
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
