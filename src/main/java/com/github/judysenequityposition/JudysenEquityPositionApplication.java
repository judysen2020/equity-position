package com.github.judysenequityposition;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.github.judysenequityposition.domain.mapper")
public class JudysenEquityPositionApplication {

    public static void main(String[] args) {
        SpringApplication.run(JudysenEquityPositionApplication.class, args);
    }
}
