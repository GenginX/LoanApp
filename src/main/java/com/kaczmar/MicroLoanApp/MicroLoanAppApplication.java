package com.kaczmar.MicroLoanApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableSpringDataWebSupport
public class MicroLoanAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroLoanAppApplication.class, args);
    }

}
