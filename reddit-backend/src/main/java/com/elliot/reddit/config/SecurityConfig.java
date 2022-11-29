package com.elliot.reddit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// Disable CSRF
				.csrf().disable()
				// Authorize anyone access the authentication route
				.authorizeRequests()
				.antMatchers("/api/auth/**")
				.permitAll()
				// Require being authenticated for any other routes
				.anyRequest()
				.authenticated();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}