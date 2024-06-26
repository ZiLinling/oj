package com.xmut.onlinejudge.interceptor;


import com.xmut.onlinejudge.exception.tokenExpiredException;
import com.xmut.onlinejudge.utils.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws Exception {
        String token = req.getHeader("token");
        System.out.println(req.getRequestURI());
        //在拦截器中设置允许跨域(拦截器需设置跨域)
        if (!JwtUtil.verifyToken(token))
            throw new tokenExpiredException();
        return true;
    }
}