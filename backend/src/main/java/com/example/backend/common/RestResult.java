package com.example.backend.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/2/26 16:43
 */
@Data
public class RestResult<T> {
    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> RestResult<T> success(T object,String msg) {
        RestResult<T> r = new RestResult<T>();
        r.msg = msg;
        r.data = object;
        r.code = 200;
        return r;
    }

    public static <T> RestResult<T> error(String msg,Integer code) {
        RestResult r = new RestResult();
        r.msg = msg;
        r.code = code;
        return r;
    }

    public RestResult<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
