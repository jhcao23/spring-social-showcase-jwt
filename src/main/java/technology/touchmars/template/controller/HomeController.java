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
package technology.touchmars.template.controller;

import java.security.Principal;

import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import technology.touchmars.template.repository.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	private Provider<ConnectionRepository> connectionRepositoryProvider;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Principal currentUser, Model model) {
		if (currentUser != null) {
			ConnectionRepository cp = getConnectionRepository();
			System.out.println("ConnectionRepository==null::"+(cp.getClass()));
//			model.addAttribute("connectionsToProviders", cp.findAllConnections());
			if (currentUser != null) {
				model.addAttribute(userRepository.findByHashId(currentUser.getName()));
			}
		}
		return "home";
	}
	
	private ConnectionRepository getConnectionRepository() {
		return connectionRepositoryProvider.get();
	}
}
