package org.springframework.social.showcase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.showcase.model.TouchUser;
import org.springframework.social.showcase.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoRestController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/allusers")
	public List<TouchUser> getAllUsers(){
		return userRepository.findAll();		
	}
	
	@GetMapping("/count")
	public long getCount(){
		return userRepository.count();		
	}
	
}
