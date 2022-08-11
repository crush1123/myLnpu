package com.MyLNPU.controller.utils;

import com.MyLNPU.model.responseModel.Response;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Spring MVC的异常处理器
 */
@RestControllerAdvice
public class ProjectExceptionAdvice {
    @ExceptionHandler(NullPointerException.class)
    public String NotFundParamException(NullPointerException e){
        e.printStackTrace();
        return JSON.toJSONString(new Response(0,"请检查调用接口所携带参数"));
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String NotFundParamException(MissingServletRequestParameterException e){
        e.printStackTrace();
        return JSON.toJSONString(new Response(0,"请检查调用接口所携带参数"));
    }
    @ExceptionHandler(CookieException.class)
    public String CookieException(CookieException e){
        e.printStackTrace();
        return JSON.toJSONString(new Response(-1,"Cookie已失效"));
    }
    @ExceptionHandler(PasswordErrorException.class)
    public String PasswordErrorException(PasswordErrorException e){
        e.printStackTrace();
        return JSON.toJSONString(new Response(0,"账号密码错误，请重试"));
    }
    @ExceptionHandler
    public String OtherException(Exception e){
        e.printStackTrace();
        return JSON.toJSONString(new Response(0,"服务器异常，请稍后再试"));
    }
}
