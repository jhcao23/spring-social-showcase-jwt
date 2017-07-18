package org.springframework.social.showcase.config;

import java.util.Optional;

/*
 * Copyright 2015 the original author or authors.
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

import org.springframework.security.core.Authentication;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.showcase.model.User;
import org.springframework.social.showcase.repository.UserRepository;

/**
 * Implementation of UserIdSource that returns the Spring Security {@link Authentication}'s name as the user ID.
 * @author John Cao
 */
public class AuthenticationNameUserHashIdSource extends AuthenticationNameUserIdSource {

	private UserRepository userRepository;
	
	public AuthenticationNameUserHashIdSource(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	/**
	 * Find username from regular login -> find hash id
	 */
	@Override
	public String getUserId() {
		String username = super.getUserId();
//		Optional<User> user = userRepository.findByHashIdOrAccountUsername(username, username);	//anonymousUser
		Optional<User> user = userRepository.findByHashId(username);
		if(!user.isPresent()){
			throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in::"+username);
		}else{
			User u = user.get();
			return u.getHashId();
		}
	}
	
}
