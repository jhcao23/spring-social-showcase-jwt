package org.springframework.social.showcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.social.showcase.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer>{

}
