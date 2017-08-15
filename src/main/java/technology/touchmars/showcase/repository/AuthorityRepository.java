package technology.touchmars.showcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import technology.touchmars.showcase.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer>{

}
