package org.springframework.social.showcase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.showcase.service.JwtTokenService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
		source.registerCorsConfiguration("/**", config);
		CorsFilter corsFilter = new CorsFilter(source);
		return corsFilter;
	}
	
}
