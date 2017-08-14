package org.springframework.social.showcase.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.social.showcase.model.TouchUser; 

public interface UserRepository extends JpaRepository<TouchUser, Long> {

	public Optional<TouchUser> findByAccountUsername(String username);
	public Optional<TouchUser> findByHashId(String hashId);
	public Optional<TouchUser> findByHashIdOrAccountUsername(String hashId, String username);
	
//	public default Optional<User> findByHashIdOrAccountUsername(String hashId){
//		return findByHashIdOrAccountUsername(hashId, hashId);
//	}
	
}


