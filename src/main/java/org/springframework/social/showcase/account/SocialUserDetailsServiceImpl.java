package org.springframework.social.showcase.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

public class SocialUserDetailsServiceImpl implements SocialUserDetailsService {

	private UserRepository userRepository;
	
	public SocialUserDetailsServiceImpl(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	@Override
	public SocialUserDetails loadUserByUserId(String userHashId) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByHashId(userHashId);
		if(user.isPresent()==false)
	    	throw new UsernameNotFoundException("username::"+userHashId+" not found");
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
	    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));	    
		return new SocialUser(
			userHashId, 
			user.get().getAccount()==null?null:user.get().getAccount().getPassword(), 
			grantedAuthorities
		);
	}

}
