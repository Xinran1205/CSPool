package com.example.backend.filter;

import com.alibaba.fastjson.JSON;
import com.example.backend.common.BaseContext;
import com.example.backend.common.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Xinran
 * @version 1.0
 * @description 进行权限校验，不登陆的人，有些界面看不了，并且
 * @date 2023/3/2 10:12
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();// /backend/index.html

        //大括号是占位符，后面requestURI直接填充这个
        //调试
        log.info("拦截到请求：{}",requestURI);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
                //还有我们一上来的界面都是可以访问，用户不用登录就可以查询视频，不需要强制用户登录
        };

        //用户不能访问以下url
        String[] urls2 = new String[]{
                "/backend/**",
                "/common/**",
        };

        //2、判断本次请求是否需要处理
        boolean check1 = check(urls, requestURI);

        boolean check2 = check(urls2, requestURI);

        //3、如果不需要处理，则直接放行
        //doFilter就是放行
        if(check1){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //4-1、判断员工登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));

            //这里就把获得的当前用户id给到threadlocal
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        //4-2、判断用户登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            //如果用户登录了，但是它要访问的页面是管理员才能访问的页面，那么我们也不能放行
            if(!check2) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        log.info("用户未登录");
        //5、如果未登录则返回未登录结果（不放行），通过输出流方式向客户端页面响应数据，使得前端根据输出进行跳转
        response.getWriter().write(JSON.toJSONString(RestResult.error("NOTLOGIN",0)));
        return;

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
