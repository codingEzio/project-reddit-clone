package com.elliot.reddit;

import com.elliot.reddit.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class RedditBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditBackendApplication.class, args);
	}

}
