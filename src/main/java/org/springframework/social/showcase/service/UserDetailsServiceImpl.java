package org.springframework.social.showcase.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.showcase.model.Authority;
import org.springframework.social.showcase.model.User;
import org.springframework.social.showcase.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public org.springframework.security.core.userdetails.User loadUserByUsername(String username) throws UsernameNotFoundException {
	    Optional<User> user = userRepository.findByAccountUsername(username);
	    if(user.isPresent()==false)
	    		throw new UsernameNotFoundException("username::"+username+" not found");
	    List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
	    for(Authority a: user.get().getAuthorityList()) {
	    		grantedAuthorities.add(new SimpleGrantedAuthority(a.getAuthorityName()));
	    }
	    return new org.springframework.security.core.userdetails.User(user.get().getHashId(), user.get().getAccount().getPassword(), grantedAuthorities);
	}

}
