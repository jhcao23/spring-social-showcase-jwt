package technology.touchmars.showcase.repository;

import java.util.List;

import org.springframework.util.MultiValueMap;

import technology.touchmars.showcase.model.UserConnection;

public interface AdvancedUserConnectionRepository {

	public List<UserConnection> findByUserIdAndMap(Long userId, MultiValueMap<String, String> providerUsers);
	
}
