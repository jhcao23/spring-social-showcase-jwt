package org.springframework.social.showcase.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CorsConfig {

	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
            	registry.addMapping("/**")
            		.allowedMethods(GET.name())
            		.allowedMethods(PUT.name())
            		.allowedMethods(POST.name())
            		.allowedMethods(OPTIONS.name())
            		.allowCredentials(true)
            		.allowedHeaders("*")
            		.allowedOrigins("*")
            	;
            	
            }
        };
    }
	
}
