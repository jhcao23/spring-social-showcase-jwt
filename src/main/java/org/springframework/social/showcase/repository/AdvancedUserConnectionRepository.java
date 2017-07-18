package org.springframework.social.showcase.repository;

import java.util.List;

import org.springframework.social.showcase.model.UserConnection;
import org.springframework.util.MultiValueMap;

public interface AdvancedUserConnectionRepository {

	public List<UserConnection> findByUserIdAndMap(Integer userId, MultiValueMap<String, String> providerUsers);
	
}
