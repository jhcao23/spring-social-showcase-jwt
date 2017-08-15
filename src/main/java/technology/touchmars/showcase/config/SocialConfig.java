package technology.touchmars.showcase.config;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.authentication.JwtSocialAuthenticationSuccessHandler;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;

import technology.touchmars.showcase.repository.AdvancedUserConnectionRepository;
import technology.touchmars.showcase.repository.AdvancedUserConnectionRepositoryImpl;
import technology.touchmars.showcase.repository.AuthorityRepository;
import technology.touchmars.showcase.repository.JpaUsersConnectionRepository;
import technology.touchmars.showcase.repository.UserConnectionRepository;
import technology.touchmars.showcase.repository.UserRepository;
import technology.touchmars.showcase.service.JpaConnectionSignUp;
import technology.touchmars.showcase.service.SocialUserDetailsServiceImpl;

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
	
	@Override
	@Bean
	@Primary
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
	@Bean
	@Primary
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
