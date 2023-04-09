package com.loginsocial;

import com.loginsocial.configuration.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class LoginsocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginsocialApplication.class, args);
	}

}
