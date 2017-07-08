package org.springframework.social.showcase.config;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.authentication.JwtSocialAuthenticationSuccessHandler;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.showcase.account.AdvancedUserConnectionRepository;
import org.springframework.social.showcase.account.AdvancedUserConnectionRepositoryImpl;
import org.springframework.social.showcase.account.SocialUserDetailsServiceImpl;
import org.springframework.social.showcase.account.UserConnectionRepository;
import org.springframework.social.showcase.account.UserRepository;

@Configuration
public class SocialConfig extends SocialConfigurerAdapter{

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private UserConnectionRepository userConnectionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TextEncryptor textEncryptor;

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
		return new JpaConnectionSignUp(userRepository);
	}
	
    @Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserHashIdSource(userRepository);
	}
    
    //TODO: removed ProviderSignInUtils & ConnectController beans so that fresh social login 
    //		won't trigger UserDetailsService.loadUserByUsername, why?
    
    @Bean
    public JwtSocialAuthenticationSuccessHandler getJwtSocialAuthenticationSuccessHandler(){
    	return new JwtSocialAuthenticationSuccessHandler(userRepository);
    }
   
}
