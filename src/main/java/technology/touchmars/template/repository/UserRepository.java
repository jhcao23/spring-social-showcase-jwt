package technology.touchmars.template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import technology.touchmars.template.model.TouchUser; 

public interface UserRepository extends JpaRepository<TouchUser, Long> {

	public Optional<TouchUser> findByAccountUsername(String username);
	public Optional<TouchUser> findByHashId(String hashId);
	public Optional<TouchUser> findByHashIdOrAccountUsername(String hashId, String username);
	
}


