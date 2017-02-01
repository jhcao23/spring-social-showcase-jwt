package org.springframework.social.showcase.account;

import java.util.List;

import org.springframework.util.MultiValueMap;

public interface AdvancedUserConnectionRepository {

	public List<UserConnection> findByUserIdAndMap(Integer userId, MultiValueMap<String, String> providerUsers);
	
}
