package com.vorix.authservice;

import com.vorix.authservice.config.AppProperties;
import com.vorix.authservice.security.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, AppProperties.class})
public class VorixAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VorixAuthServiceApplication.class, args);
	}

}
