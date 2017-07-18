package org.springframework.social.showcase.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.social.showcase.model.ConnectionProperty;
import org.springframework.social.showcase.model.UserConnection;
import org.springframework.stereotype.Repository;

public interface UserConnectionRepository extends JpaRepository<UserConnection, Long> {

	@Query("select user.id from UserConnection uc where uc.providerId = ?1 and uc.providerUserId in ?2")
	public List<Integer> findUserIdsByProviderIdAndProviderUserIds(String providerId, Set<String> providerUserIds);
	
	@Query("select u.hashId from UserConnection uc left join uc.user u where uc.providerId = ?1 and uc.providerUserId in ?2")
	public List<String> findUserHashIdsByProviderIdAndProviderUserIds(String providerId, Set<String> providerUserIds);
	
	@Query("select u.hashId from UserConnection uc left join uc.user u where uc.providerId = ?1 and uc.providerUserId = ?2")
	public List<String> findUserHashIdsByProviderIdAndProviderUserId(String providerId, String providerUserId);
	
	public List<ConnectionProperty> findByUserIdAndProviderIdOrderByRankAsc(Integer id, String providerId);
	
	public Optional<ConnectionProperty> findTopByUserIdAndProviderIdOrderByRankAsc(Integer id, String providerId);
	
	public List<ConnectionProperty> findByUserIdOrderByProviderIdAscRankAsc(Integer id);
	
	public Optional<UserConnection> findByUserIdAndProviderIdAndProviderUserId(Integer id, String providerId, String providerUserId);
	
	@Query("select coalesce(max(uc.rank) + 1, 1) as rank from UserConnection uc where uc.user.id = ?1 and uc.providerId = ?2")
	public Integer findNextRankByUserIdAndProviderId(Integer id, String providerId);
		
//	@Query("delete from UserConnection uc where uc.user.id = ?1 and uc.providerId = ?2")
//	@Modifying
	@Transactional
	public void deleteByUserIdAndProviderId(Integer id, String providerId);	
//	public void deleteByProviderId(String providerId);
	
//	@Modifying
	@Transactional
	public Integer deleteByUserIdAndProviderIdAndProviderUserId(Integer id, String providerId, String providerUserId);

}


