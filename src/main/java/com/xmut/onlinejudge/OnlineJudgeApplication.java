package com.xmut.onlinejudge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.xmut.onlinejudge.mapper")
public class OnlineJudgeApplication {


    public static void main(String[] args) {
        SpringApplication.run(OnlineJudgeApplication.class, args);
    }

}


