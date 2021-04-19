package com.huayun.bond;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.huayun.bond.dao")
public class BondApplication {
    public static void main(String[] args){
        SpringApplication.run(BondApplication.class, args);
    }
}
