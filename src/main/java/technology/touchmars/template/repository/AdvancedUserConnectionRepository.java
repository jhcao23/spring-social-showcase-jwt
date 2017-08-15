package technology.touchmars.template.repository;

import java.util.List;

import org.springframework.util.MultiValueMap;

import technology.touchmars.template.model.UserConnection;

public interface AdvancedUserConnectionRepository {

	public List<UserConnection> findByUserIdAndMap(Long userId, MultiValueMap<String, String> providerUsers);
	
}
