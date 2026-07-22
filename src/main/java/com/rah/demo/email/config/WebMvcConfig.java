package com.rah.demo.email.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final HandlerInterceptorConfig handlerInterceptorConfig;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.handlerInterceptorConfig);
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	
	
}
