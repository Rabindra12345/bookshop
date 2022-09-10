package com.rabindra.bookmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableSwagger2
@PropertySource("classpath:application-${spring.profiles.active:default}.properties")
public class BookmarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmarketApplication.class, args);
	}

}
