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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.showcase.message.Message;
import org.springframework.social.showcase.message.MessageType;
import org.springframework.social.showcase.model.Authority;
import org.springframework.social.showcase.model.User;
import org.springframework.social.showcase.repository.AuthorityRepository;
import org.springframework.social.showcase.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Controller
public class SignupController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthorityRepository authorityRepository;
	
	private ProviderSignInUtils providerSignInUtils;

	@Inject
	public SignupController(
		                    ConnectionFactoryLocator connectionFactoryLocator,
		                    UsersConnectionRepository connectionRepository) {
		this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
	}

	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public SignupForm signupForm(WebRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		if (connection != null) {
			request.setAttribute("message", new Message(MessageType.INFO, "Your " + StringUtils.capitalize(connection.getKey().getProviderId()) + " account is not associated with a Spring Social Showcase account. If you're new, please sign up."), WebRequest.SCOPE_REQUEST);
			return SignupForm.fromProviderUser(connection.fetchUserProfile());
		} else {
			return new SignupForm();
		}
	}

	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request, HttpServletResponse response) {
		if (formBinding.hasErrors() || form==null) {
			return null;
		}
		Authority authority = authorityRepository.findOne(Authority.ID_ROLE_USER);
		User user = createUser(form, formBinding, authority);
		if (user != null) {
			SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(user.getHashId(), null, null)
			);
			providerSignInUtils.doPostSignUp(user.getHashId(), request);
			String token = getToken4User(user);
			response.addHeader(AUTH_HEADER_NAME, token);
			return null;
		}
		return null;
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
