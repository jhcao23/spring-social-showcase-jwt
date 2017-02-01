package org.springframework.social.showcase.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository; 

public interface UserRepository extends JpaRepository<User, Integer> {

	public Optional<User> findByAccountUsername(String username);
	public Optional<User> findByHashId(String hashId);
	public Optional<User> findByHashIdOrAccountUsername(String hashId, String username);
	
}


