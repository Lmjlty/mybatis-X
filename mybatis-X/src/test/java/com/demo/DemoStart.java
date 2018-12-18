package com.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import com.lmj.base.interceptors.ChangeReturnClassInterceptor;
import com.lmj.base.interceptors.SQLInterceptor;


@SpringBootApplication
@MapperScan("com.mapper")
public class DemoStart {
    public static void main(String[] args) {
        new SpringApplicationBuilder(
                DemoStart.class)
               .web(true).run(args);
    }
    //SQL打印
    @Bean
    public SQLInterceptor sqlInterceptor() {
        return new SQLInterceptor();
    }
    
}
