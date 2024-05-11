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
    public Result<String> errorHandlerToken(HttpServletRequest request, Exception ex) {
        Result<String> result = new Result<>();
        result.error("Token Expired");
        return result;
    }

}
