package com.xmut.onlinejudge.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.xmut.onlinejudge.entity.User;

import java.util.Date;

public class JwtUtil {
    private static Algorithm algorithm = Algorithm.HMAC256("asdwqewqhidhwhdqwh");

    static public String generateToken(User user) {
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60 * 60 * 1000 * 24;//24小时
        Date end = new Date(currentTime);
        String token = "";
        token = JWT.create()
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withIssuedAt(start)
                .withExpiresAt(end)
                .sign(algorithm);
        return token;
    }

    static public Object parseToken(String token, String key) {
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token).getClaim(key).as(Object.class);
    }

    static public Integer getUserId(String token) {
        return (Integer) parseToken(token, "id");
    }

    static public String getUsername(String token) {
        return (String) parseToken(token, "username");
    }


    static public Boolean verifyToken(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            jwtVerifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}