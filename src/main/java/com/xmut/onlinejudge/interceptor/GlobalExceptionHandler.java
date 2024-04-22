package com.xmut.onlinejudge.interceptor;

import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.exception.tokenExpiredException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@CrossOrigin
public class GlobalExceptionHandler {

    @ExceptionHandler(value = tokenExpiredException.class)
    public Result errorHandlertoken(HttpServletRequest request, Exception ex) {
        Result result = new Result();
        result.error("Token Expired");
        return result;
    }
}
