package org.springframework.social.showcase.service;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.showcase.model.Authority;
import org.springframework.social.showcase.model.User;
import org.springframework.social.showcase.repository.AuthorityRepository;
import org.springframework.social.showcase.repository.UserRepository;

public class JpaConnectionSignUp implements ConnectionSignUp {

	private UserRepository userRepository;
	private AuthorityRepository authorityRepository;
	
	public JpaConnectionSignUp(UserRepository userRepository, AuthorityRepository authorityRepository){
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
	}
	
	@Override
	public String execute(Connection<?> connection) {
		User user = createUser4Connection(authorityRepository);
		user = userRepository.save(user);
		return user.getHashId();
	}
	
	public static User createUser4Connection(AuthorityRepository authorityRepository) {
		User user = new User();
		String hashId = GenerateUniqueKey.getInstance().generateUniqueKeyUsingMessageDigest();
		user.setHashId(hashId);
		//add ROLE_USER
		Authority authority = authorityRepository.findOne(Authority.ID_ROLE_USER);
		user.addAuthority(authority);		
		return user;
	}

}
