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
package org.springframework.social.showcase.signup;

import static org.springframework.social.showcase.service.JwtTokenService.AUTH_HEADER_NAME;
import static org.springframework.social.showcase.service.JwtTokenService.getToken4User;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.showcase.model.Authority;
import org.springframework.social.showcase.model.User;
import org.springframework.social.showcase.repository.AuthorityRepository;
import org.springframework.social.showcase.repository.UserRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
public class RestSignupController {
	
	private final Logger logger = LoggerFactory.getLogger(RestSignupController.class);

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthorityRepository authorityRepository;
	
	private ProviderSignInUtils providerSignInUtils;

	@Inject
	public RestSignupController(
		ConnectionFactoryLocator connectionFactoryLocator,
		UsersConnectionRepository connectionRepository) {
		this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
	}

	@PostMapping(value="/rest/signup")
	public ResponseEntity<String> signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request, HttpServletResponse response) {
		if (formBinding.hasErrors() || form==null) {
			return ResponseEntity.badRequest().body("invalid signup form " + formBinding.toString());
		}
		Authority authority = authorityRepository.findOne(Authority.ID_ROLE_USER);
		try {
			String username = form.getUsername();
			if(userRepository.findByAccountUsername(username).isPresent()) {
				return ResponseEntity.status(HttpStatus.IM_USED).body("username is already used!");//226
			}
			User user = createUser(form, formBinding, authority);
			//successfully created a User
			if (user != null) {
				SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(user.getHashId(), null, null)
				);
				providerSignInUtils.doPostSignUp(user.getHashId(), request);
				String token = getToken4User(user);
				response.addHeader(AUTH_HEADER_NAME, token);
				return ResponseEntity.ok().build();//200
			}
		}catch(DataAccessException e) {
			logger.debug("DataAccessException: {}", e.getMessage());
		}catch(Exception e) {
			logger.error("Error: {}", e.getMessage());
		}
		return ResponseEntity.badRequest().body("internal error");//400
	}

	// internal helpers
	
	private User createUser(SignupForm form, BindingResult formBinding, Authority authority) {
		try {
			User user = new User();
			user.assembleUser(form.getUsername(), form.getPassword(), form.getFirstName(), form.getLastName());
			user.addAuthority(authority);
			return userRepository.save(user);			
		} catch (Exception e) {
			e.printStackTrace();
			formBinding.rejectValue("username", "user.duplicateUsername", "already in use");
			return null;
		}
	}

}
