package org.springframework.social.showcase.repository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.showcase.model.User;
import org.springframework.stereotype.Repository;

public class JpaUsersConnectionRepository implements UsersConnectionRepository {

	private UserRepository userRepository;
	
	private UserConnectionRepository userConnectionRepository;
	
	private AdvancedUserConnectionRepository advancedUserConnectionRepository;
	
	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;

	private ConnectionSignUp connectionSignUp;
	
	public JpaUsersConnectionRepository(
			UserRepository userRepository,
			UserConnectionRepository userConnectionRepository, 
			AdvancedUserConnectionRepository advancedUserConnectionRepository,
			ConnectionFactoryLocator connectionFactoryLocator,
			TextEncryptor textEncryptor) {
		this.userRepository = userRepository;
		this.userConnectionRepository = userConnectionRepository;
		this.advancedUserConnectionRepository = advancedUserConnectionRepository;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;	
	}

	/**
	 * The command to execute to create a new local user profile in the event no user id could be mapped to a connection.
	 * Allows for implicitly creating a user profile from connection data during a provider sign-in attempt.
	 * Defaults to null, indicating explicit sign-up will be required to complete the provider sign-in attempt.
	 * @param connectionSignUp a {@link ConnectionSignUp} object
	 * @see #findUserIdsWithConnection(Connection)
	 */
	public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
		this.connectionSignUp = connectionSignUp;
	}
	
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		List<String> list = userConnectionRepository.findUserHashIdsByProviderIdAndProviderUserIds(providerId, providerUserIds);
		return new HashSet<String>(list);
	}
	
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		ConnectionKey key = connection.getKey();
		List<String> localUserHashIds = 
				userConnectionRepository.findUserHashIdsByProviderIdAndProviderUserId(
					key.getProviderId(), key.getProviderUserId()
				);
		if (localUserHashIds.size() == 0 && connectionSignUp != null) {
			String newUserHashId = connectionSignUp.execute(connection);
			if (newUserHashId != null)
			{
				createConnectionRepository(newUserHashId).addConnection(connection);
				return Arrays.asList(newUserHashId);
			}
		}
		return localUserHashIds;
	}

	public ConnectionRepository createConnectionRepository(String username) {
		if (username == null) {
			throw new IllegalArgumentException("userId cannot be null");
		}
		Optional<User> user = userRepository.findByHashId(username);
		if(user.isPresent()==false){
			System.out.println("createConnectionRepository::"+username+" failed 1st time!");
//			user = userRepository.findByHashId(username);
//			if(user.isPresent()==false)
//				return null;
		}
			
		return new JpaConnectionRepository(
			user.get().getId(), userRepository, 
			userConnectionRepository, advancedUserConnectionRepository, 
			connectionFactoryLocator, textEncryptor
		);
	}
	
}
