package com.example.demo_login_auth_final;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.demo_login_auth_final.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class DemoLoginAuthFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoLoginAuthFinalApplication.class, args);
	}

}
