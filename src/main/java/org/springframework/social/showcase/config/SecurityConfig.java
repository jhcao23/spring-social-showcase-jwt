/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.showcase.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.JwtAuthenticationFilter;
import org.springframework.security.web.authentication.JwtSocialAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.JwtUsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.social.UserIdSource;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.social.showcase.account.UserRepository;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

/**
 * Security Configuration.
 * @author Craig Walls
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserIdSource userIdSource;
	@Autowired
	private JwtSocialAuthenticationSuccessHandler jwtSocialAuthenticationSuccessHandler;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**/*.css", "/**/*.png", "/**/*.gif", "/**/*.jpg");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
//			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.formLogin()
				.loginPage("/login")			//"signin" will cause a crazy error
				.usernameParameter("username")
				.passwordParameter("password")
				.loginProcessingUrl("/signin/authenticate")
				.defaultSuccessUrl("/connect")
				.failureUrl("/signin?error=bad_credentials")
				.permitAll()
			.and()
				.logout()
					.logoutUrl("/signout")
					.deleteCookies("JSESSIONID")
					.permitAll()
			.and()
				.authorizeRequests()
					.antMatchers("/", "/webjars/**", "/admin/**", "/favicon.ico", "/resources/**", "/auth/**", "/signin/**", "/signup/**", "/disconnect/facebook").permitAll()
					.antMatchers("/**").authenticated()
			.and()
				.rememberMe()
			.and()
				.addFilterBefore(new JwtAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
            	.addFilterBefore(getUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.apply(getSpringSocialConfigurer())
			;
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	    auth
	    	.userDetailsService(userDetailsService)
	    	.passwordEncoder(passwordEncoder())
	    ;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
	}
	
	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}
	
	 //JWT Stateless

    public JwtUsernamePasswordAuthenticationFilter getUsernamePasswordAuthenticationFilter() throws Exception{
    	JwtUsernamePasswordAuthenticationFilter filter = new JwtUsernamePasswordAuthenticationFilter(userRepository);
    	filter.setAuthenticationManager(this.authenticationManager());
    	return filter;
    }
    
    public SpringSocialConfigurer getSpringSocialConfigurer(){
    	SpringSocialConfigurer ssc = new SpringSocialConfigurer();
    	ssc.userIdSource(userIdSource);
    	ssc.addObjectPostProcessor(new ObjectPostProcessor<SocialAuthenticationFilter>(){
    		@Override
			public <O extends SocialAuthenticationFilter> O postProcess(O filter){
				filter.setAuthenticationSuccessHandler(jwtSocialAuthenticationSuccessHandler);
				return filter;
			}
		 });
    	return ssc;
    }

}
