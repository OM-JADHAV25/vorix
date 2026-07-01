package com.vorix.gitservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.vorix.gitservice.config.GitHubAppProperties;

@SpringBootApplication
@EnableConfigurationProperties(GitHubAppProperties.class)
public class GitServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitServiceApplication.class, args);
	}

}
