package technology.touchmars.template.service;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import technology.touchmars.template.model.Authority;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.repository.AuthorityRepository;
import technology.touchmars.template.repository.UserRepository;

public class JpaConnectionSignUp implements ConnectionSignUp {

	private UserRepository userRepository;
	private AuthorityRepository authorityRepository;
	
	public JpaConnectionSignUp(UserRepository userRepository, AuthorityRepository authorityRepository){
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
	}
	
	@Override
	public String execute(Connection<?> connection) {
		TouchUser user = createUser4Connection(authorityRepository);
		user = userRepository.save(user);
		return user.getHashId();
	}
	
	public static TouchUser createUser4Connection(AuthorityRepository authorityRepository) {
		TouchUser user = new TouchUser();
		String hashId = GenerateUniqueKey.getInstance().generateUniqueKeyUsingMessageDigest();
		user.setHashId(hashId);
		//add ROLE_USER
		Authority authority = authorityRepository.findOne(Authority.ID_ROLE_USER);
		user.addAuthority(authority);		
		return user;
	}

}
