package technology.touchmars.template.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public org.springframework.security.core.userdetails.User loadUserByUsername(String username) throws UsernameNotFoundException {
	    Optional<TouchUser> user = userRepository.findByAccountUsername(username);
	    if(user.isPresent()==false)
	    		throw new UsernameNotFoundException("username::"+username+" not found");
	    List<? extends GrantedAuthority> grantedAuthorities = user.get().getSimpleGrantedAuthorityList();//new ArrayList<GrantedAuthority>();
//	    for(Authority a: user.get().getAuthorityList()) {
//	    		grantedAuthorities.add(new SimpleGrantedAuthority(a.getName()));
//	    }
	    return new org.springframework.security.core.userdetails.User(user.get().getHashId(), user.get().getAccount().getPassword(), grantedAuthorities);
	}

}
