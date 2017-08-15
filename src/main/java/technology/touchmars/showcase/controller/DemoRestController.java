package technology.touchmars.showcase.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import technology.touchmars.showcase.model.TouchUser;
import technology.touchmars.showcase.repository.UserRepository;

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
