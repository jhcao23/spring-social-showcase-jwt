package org.springframework.social.showcase.config;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.authentication.JwtSocialAuthenticationSuccessHandler;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.MobileSecurityEnabledConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.showcase.repository.AdvancedUserConnectionRepository;
import org.springframework.social.showcase.repository.AdvancedUserConnectionRepositoryImpl;
import org.springframework.social.showcase.repository.AuthorityRepository;
import org.springframework.social.showcase.repository.JpaUsersConnectionRepository;
import org.springframework.social.showcase.repository.UserConnectionRepository;
import org.springframework.social.showcase.repository.UserRepository;
import org.springframework.social.showcase.service.JpaConnectionSignUp;
import org.springframework.social.showcase.service.SocialUserDetailsServiceImpl;

@Configuration
public class SocialConfig extends SocialConfigurerAdapter{

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private UserConnectionRepository userConnectionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private TextEncryptor textEncryptor;
	
	@Autowired
	private Environment environment;
	@Autowired
	private List<SocialConfigurer> socialConfigurers;

	@Bean	
	@Primary
	public ConnectionFactoryLocator getConnectionFactoryLocator() {
		MobileSecurityEnabledConnectionFactoryConfigurer cfConfig = new MobileSecurityEnabledConnectionFactoryConfigurer();
		for (SocialConfigurer socialConfigurer : socialConfigurers) {
			socialConfigurer.addConnectionFactories(cfConfig, environment);
		}
		return cfConfig.getConnectionFactoryLocator();		
	}
	
	@Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		JpaUsersConnectionRepository usersConnectionRepository = new 
			JpaUsersConnectionRepository(
				userRepository, 
				userConnectionRepository, 
				getAdvancedUserConnectionRepository(),
				connectionFactoryLocator, 
				textEncryptor
			);        	
		usersConnectionRepository.setConnectionSignUp(getConnectionSignUp());
		return usersConnectionRepository;
	}	
	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserHashIdSource(userRepository);
	}
    
	@Bean
	public AdvancedUserConnectionRepository getAdvancedUserConnectionRepository(){
		return new AdvancedUserConnectionRepositoryImpl(entityManager);
	}	
	@Bean
	public SocialUserDetailsService getSocialUserDetailsService(){
		return new SocialUserDetailsServiceImpl(userRepository);
	}	
	@Bean
	public ConnectionSignUp getConnectionSignUp(){
		return new JpaConnectionSignUp(userRepository, authorityRepository);
	}
	    
    //TODO: removed ProviderSignInUtils & ConnectController beans so that fresh social login 
    //		won't trigger UserDetailsService.loadUserByUsername, why?
    
    @Bean
    public JwtSocialAuthenticationSuccessHandler getJwtSocialAuthenticationSuccessHandler(){
    	return new JwtSocialAuthenticationSuccessHandler(userRepository);
    }
    
}
