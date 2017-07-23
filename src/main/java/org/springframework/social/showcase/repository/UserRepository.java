package org.springframework.social.showcase.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.social.showcase.model.User; 

public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByAccountUsername(String username);
	public Optional<User> findByHashId(String hashId);
	public Optional<User> findByHashIdOrAccountUsername(String hashId, String username);
	
//	public default Optional<User> findByHashIdOrAccountUsername(String hashId){
//		return findByHashIdOrAccountUsername(hashId, hashId);
//	}
	
}


