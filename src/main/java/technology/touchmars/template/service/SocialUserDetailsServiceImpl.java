package technology.touchmars.template.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

import technology.touchmars.template.model.Authority;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.repository.UserRepository;

public class SocialUserDetailsServiceImpl implements SocialUserDetailsService {

	private UserRepository userRepository;
	
	public SocialUserDetailsServiceImpl(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	@Override
	public SocialUserDetails loadUserByUserId(String userHashId) throws UsernameNotFoundException {
		Optional<TouchUser> user = userRepository.findByHashId(userHashId);
		if(user.isPresent()==false)
	    		throw new UsernameNotFoundException("username::"+userHashId+" not found");
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for(Authority a: user.get().getAuthorityList()) {
	    		grantedAuthorities.add(new SimpleGrantedAuthority(a.getName()));
	    }	    
		return new SocialUser(
			userHashId, 
			user.get().getAccount()==null?"":user.get().getAccount().getPassword(), //TODO: empty password MIGHT be an issue
			grantedAuthorities
		);
	}

}
