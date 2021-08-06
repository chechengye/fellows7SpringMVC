package com.lovecoding.config;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView mav = new ModelAndView();
        System.out.println("ExceptionMsg = " + e);//将错误信息输入到服务端日志文件中
        mav.addObject("ExceptionMsg", "页面走丢了");
        mav.setViewName("error");
        return mav;
    }
}
