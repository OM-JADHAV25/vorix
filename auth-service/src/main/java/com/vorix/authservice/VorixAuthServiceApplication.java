package com.vorix.authservice;

import com.vorix.authservice.config.AppProperties;
import com.vorix.authservice.config.SecurityProperties;
import com.vorix.authservice.security.jwt.JwtProperties;
import com.vorix.authservice.security.oauth.GitHubOAuthProperties;
import com.vorix.authservice.security.oauth.GoogleOAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties({JwtProperties.class, AppProperties.class, SecurityProperties.class, GoogleOAuthProperties.class, GitHubOAuthProperties.class})
public class VorixAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VorixAuthServiceApplication.class, args);
	}

}
