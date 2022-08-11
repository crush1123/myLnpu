package com.MyLNPU;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class MyLnpuApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyLnpuApplication.class, args);
    }

}
