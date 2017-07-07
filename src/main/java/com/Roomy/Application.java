package com.Roomy;

import java.sql.SQLException;
import java.text.ParseException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@Configuration
@PropertySource("classpath:/application.properties")
public class Application {
	public static void main(String[] args) throws SQLException, ParseException {
		SpringApplication.run(Application.class, args);

	}

}
