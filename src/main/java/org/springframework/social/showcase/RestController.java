package org.springframework.social.showcase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.showcase.account.User;
import org.springframework.social.showcase.account.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/allusers")
	public List<User> getAllUsers(){
		return userRepository.findAll();		
	}
	
	@GetMapping("/count")
	public long getCount(){
		return userRepository.count();		
	}
	
	
}
