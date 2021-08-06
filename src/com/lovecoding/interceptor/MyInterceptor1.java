package com.lovecoding.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor1 implements HandlerInterceptor {

    /**
     *  preHandle 方法的返回值 。false代表拦截  true代表放行
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws Exception {
        System.out.println("前拦截处理方法...");
        //拦截处理： 用户登录时携带 token 令牌 （每个用户登录之后 都会生成一个token  - 放置用户访问接口的权限）
        String token = "test";//req.getHeader("token");
        if(token != null){
            //权限校验通过放行
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("后拦截处理方法...");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("最终拦截处理方法...");
    }
}
