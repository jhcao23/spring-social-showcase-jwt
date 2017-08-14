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
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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
import org.springframework.security.web.authentication.WechatMiniAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.social.UserIdSource;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.social.showcase.repository.AuthorityRepository;
import org.springframework.social.showcase.repository.UserConnectionWechatRepository;
import org.springframework.social.showcase.repository.UserRepository;
import org.springframework.social.showcase.service.WechatMiniProgramService;
import org.springframework.web.filter.CorsFilter;
//import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import technology.touchmars.feign.wechat.client.api.MiniProgramUnionApiClient;

/**
 * Security Configuration.
 * @author Craig Walls
 */
@Configuration
//@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserIdSource userIdSource;
	@Autowired
	private JwtSocialAuthenticationSuccessHandler jwtSocialAuthenticationSuccessHandler;
	
	@Autowired
	private UserRepository userRepository;	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthorityRepository authorityRepository;

	// wechat mini program
	@Autowired
	private WechatMiniProgramService wechatMiniProgramService;
	@Autowired
	private UserConnectionWechatRepository userConnectionWechatRepository;
	@Autowired
	private MiniProgramUnionApiClient miniProgramUnionApiClient;
	
	@Autowired
	private CorsFilter corsFilter;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**/*.css", "/**/*.png", "/**/*.gif", "/**/*.jpg");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.csrf().disable()
//				.formLogin()
//				.loginPage("/login")			//"signin" will cause a crazy error
//				.usernameParameter("username")
//				.passwordParameter("password")
//				.loginProcessingUrl("/signin/authenticate")
//				.defaultSuccessUrl("/connect")
//				.failureUrl("/signin?error=bad_credentials")
//				.permitAll()
//			.and()
//				.logout()
//					.logoutUrl("/signout")
//					.deleteCookies("JSESSIONID")
//					.permitAll()
//			.and()
				.authorizeRequests()
					.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()		
					.antMatchers("/rest/signup").permitAll()
//					.antMatchers("/rest/signin").permitAll()
					.antMatchers("/", "/webjars/**", "/admin/**", "/favicon.ico", "/resources/**", "/auth/**", "/signin/**", "/signup/**", "/disconnect/facebook").permitAll()
					.antMatchers("/**").authenticated()				
//			.and().exceptionHandling()
//			.and()
//				.rememberMe()
			.and()
				.addFilterBefore(new JwtAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
				.addFilterBefore(getWechatMiniAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
				.addFilterBefore(corsFilter, JwtAuthenticationFilter.class)
				.addFilterBefore(getJwtUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
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
	
//	@Bean
//	public SpringSecurityDialect springSecurityDialect() {
//		return new SpringSecurityDialect();
//	}
	
	 //JWT Stateless

    public JwtUsernamePasswordAuthenticationFilter getJwtUsernamePasswordAuthenticationFilter() throws Exception{
	    	JwtUsernamePasswordAuthenticationFilter filter = new JwtUsernamePasswordAuthenticationFilter(userRepository);
	    	filter.setAuthenticationManager(this.authenticationManager());
	    	return filter;
    }
    
    //Wechat

	@Bean
	public WechatMiniAuthenticationFilter getWechatMiniAuthenticationFilter() throws Exception {
		WechatMiniAuthenticationFilter filter = 
			new WechatMiniAuthenticationFilter(wechatMiniProgramService,
				miniProgramUnionApiClient, userRepository, 
				userConnectionWechatRepository, authorityRepository, null);
		filter.setAuthenticationManager(this.authenticationManager());
		return filter;
	}
	
    //general Social
    
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
