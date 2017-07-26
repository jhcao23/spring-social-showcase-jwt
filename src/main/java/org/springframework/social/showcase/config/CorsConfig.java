package org.springframework.social.showcase.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.social.showcase.service.JwtTokenService;

@Configuration
public class CorsConfig {

	//Finally I solved the CORS issue which Ionic3 blocked for a while!
	//The tricky part is declare this CorsFilter and add it to SecurityConfig TOO !!!
	@Bean("corsFilter")
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addExposedHeader(JwtTokenService.AUTH_HEADER_NAME);
		config.addExposedHeader("Access-Control-Allow-Origin");
		config.addExposedHeader("Access-Control-Allow-Methods");
		config.addExposedHeader("Access-Control-Allow-Headers");
		config.addExposedHeader("Access-Control-Allow-Credentials");
		config.addExposedHeader("Cache-Control");
		config.addExposedHeader("Content-Language");
		config.addExposedHeader("Content-Type");
		config.addExposedHeader("Expires");
		config.addExposedHeader("Last-Modified");
		config.addExposedHeader("Pragma");
//		config.addExposedHeader("");
		source.registerCorsConfiguration("/**", config);
		CorsFilter corsFilter = new CorsFilter(source);
		return corsFilter;
//		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//		bean.setOrder(0);
//		return bean;
	}
	
//	@Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//            		registry.addMapping("/**")
//	            		.allowedMethods(GET.name(), PUT.name(), POST.name(), OPTIONS.name())	
//	            		.allowCredentials(true)
//	            		.allowedHeaders("*")
//	            		.allowedOrigins("*")	            		
//	            		.exposedHeaders(JwtTokenService.AUTH_HEADER_NAME)
//	            	;        		
//            	
//            }
//        };
//    }
	
}
