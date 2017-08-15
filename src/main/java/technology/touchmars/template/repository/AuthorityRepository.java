package technology.touchmars.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import technology.touchmars.template.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer>{

}
