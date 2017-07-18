package org.springframework.social.showcase.service;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.showcase.model.User;
import org.springframework.social.showcase.repository.UserRepository;

public class JpaConnectionSignUp implements ConnectionSignUp {

	private UserRepository userRepository;
	
	public JpaConnectionSignUp(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	@Override
	public String execute(Connection<?> connection) {
		User user = new User();
		String hashId = GenerateUniqueKey.getInstance().generateUniqueKeyUsingMessageDigest();
		user.setHashId(hashId);
		user = userRepository.save(user);
		return user.getHashId();
	}

}
