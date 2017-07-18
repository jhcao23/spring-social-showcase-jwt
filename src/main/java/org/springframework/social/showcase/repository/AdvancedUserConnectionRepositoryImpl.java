package org.springframework.social.showcase.repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.social.showcase.model.UserConnection;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

@Repository
public class AdvancedUserConnectionRepositoryImpl 
	extends SimpleJpaRepository<UserConnection, Long>
	implements AdvancedUserConnectionRepository {
	
	private EntityManager entityManager;

	@Inject
	public AdvancedUserConnectionRepositoryImpl(EntityManager em) {
		super(UserConnection.class, em);
		this.entityManager = em;
	}

	public List<UserConnection> findByUserIdAndMap(Integer userId, MultiValueMap<String, String> providerUsers){
		
		String query = 
			"SELECT uc "
			+ "	FROM UserConnection uc "
			+ "	where uc.user.id = :userId "
			+ "	AND ( "
		;
		
		TypedQuery<UserConnection> q = entityManager.createQuery(query, UserConnection.class);
		q = q.setParameter("userId", userId);
		for (Iterator<Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext();) {
			Entry<String, List<String>> entry = it.next();
			String providerId = entry.getKey();
			query+=" 	(	providerId = :providerId_"+providerId+" "
					+ "			and "
					+ "		providerUserId in (:providerUserIds_"+providerId+") 	"
					+"	)	";
			if (it.hasNext()) {
				query += "	OR	";
			}
			q = q.setParameter("providerId_"+providerId, providerId);
			q = q.setParameter("providerUserIds_"+providerId, entry.getValue());
		}
		query += " 	)	"
				+ "	ORDER BY providerId, rank ";
		return q.getResultList();
	}
	
}
