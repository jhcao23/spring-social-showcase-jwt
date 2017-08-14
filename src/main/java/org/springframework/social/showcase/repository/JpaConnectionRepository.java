package org.springframework.social.showcase.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.showcase.model.ConnectionProperty;
import org.springframework.social.showcase.model.TouchUser;
import org.springframework.social.showcase.model.UserConnection;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class JpaConnectionRepository implements ConnectionRepository {

	private final Long userId;
	
	private AdvancedUserConnectionRepository advancedUserConnectionRepository;
	
	private UserRepository userRepository;
	
	private UserConnectionRepository userConnectionRepository;
	
	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final TextEncryptor textEncryptor;

	public JpaConnectionRepository(
		Long userId, 
		UserRepository userRepository,
		UserConnectionRepository userConnectionRepository,
		AdvancedUserConnectionRepository advancedUserConnectionRepository,
		ConnectionFactoryLocator connectionFactoryLocator, 
		TextEncryptor textEncryptor) {		
		this.userId = userId;
		this.userRepository = userRepository;
		this.userConnectionRepository = userConnectionRepository;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}
	
	private ConnectionData convert2ConnectionData(ConnectionProperty connectionProperty){
		return new ConnectionData(
			connectionProperty.getProviderId(), connectionProperty.getProviderUserId(), 
			connectionProperty.getDisplayName(), connectionProperty.getProfileUrl(), connectionProperty.getImageUrl(), 
			decrypt(connectionProperty.getAccessToken()), decrypt(connectionProperty.getSecret()),
			decrypt(connectionProperty.getRefreshToken()), connectionProperty.getExpireTime()
		);
	}
	
	private Connection<?> convert2Connection(ConnectionProperty cp){
		ConnectionData connectionData = convert2ConnectionData(cp);
		ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
		return connectionFactory.createConnection(connectionData);
	}
	
	private List<Connection<?>> convert2Connections(List<? extends ConnectionProperty> cps){		
		return cps.stream().map(cp->convert2Connection(cp)).collect(Collectors.toList());		
	}
	
	public MultiValueMap<String, Connection<?>> findAllConnections() {

		List<ConnectionProperty> connectionPropertyList = userConnectionRepository.findByUserIdOrderByProviderIdAscRankAsc(userId);
		List<Connection<?>> resultList = convert2Connections(connectionPropertyList);
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
		for (String registeredProviderId : registeredProviderIds) {
			connections.put(registeredProviderId, Collections.<Connection<?>>emptyList());
		}
		for (Connection<?> connection : resultList) {
			String providerId = connection.getKey().getProviderId();
			if (connections.get(providerId).size() == 0) {
				connections.put(providerId, new LinkedList<Connection<?>>());
			}
			connections.add(providerId, connection);
		}
		return connections;
		
	}

	public List<Connection<?>> findConnections(String providerId) {
		List<ConnectionProperty> connectionPropertyList = 
			userConnectionRepository.findByUserIdAndProviderIdOrderByRankAsc(userId, providerId);
		return convert2Connections(connectionPropertyList);
	}

	@SuppressWarnings("unchecked")
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		List<?> connections = findConnections(getProviderId(apiType));
		return (List<Connection<A>>) connections;
	}
	
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
		if (providerUsers == null || providerUsers.isEmpty()) {
			throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
		}
		
		List<UserConnection> userConnectionList = advancedUserConnectionRepository.findByUserIdAndMap(userId, providerUsers);
		List<Connection<?>> resultList = convert2Connections(userConnectionList);
		
		MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
		for (Connection<?> connection : resultList) {
			String providerId = connection.getKey().getProviderId();
			List<String> userIds = providerUsers.get(providerId);
			List<Connection<?>> connections = connectionsForUsers.get(providerId);
			if (connections == null) {
				connections = new ArrayList<Connection<?>>(userIds.size());
				for (int i = 0; i < userIds.size(); i++) {
					connections.add(null);
				}
				connectionsForUsers.put(providerId, connections);
			}
			String providerUserId = connection.getKey().getProviderUserId();
			int connectionIndex = userIds.indexOf(providerUserId);
			connections.set(connectionIndex, connection);
		}
		return connectionsForUsers;
	}

	public Connection<?> getConnection(ConnectionKey connectionKey) {
		
		Optional<UserConnection> cp = 
			userConnectionRepository.findByUserIdAndProviderIdAndProviderUserId(
				userId, connectionKey.getProviderId(), connectionKey.getProviderUserId()
			);
		if(cp.isPresent()){
			return convert2Connection(cp.get());
		}else{
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
		String providerId = getProviderId(apiType);
		return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
	}

	@SuppressWarnings("unchecked")
	public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
		String providerId = getProviderId(apiType);
		Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
		if (connection == null) {
			throw new NotConnectedException(providerId);
		}
		return connection;
	}

	@SuppressWarnings("unchecked")
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
		String providerId = getProviderId(apiType);
		return (Connection<A>) findPrimaryConnection(providerId);
	}
	
	private UserConnection convert2UserConnection(Integer rank, ConnectionData data){
		UserConnection uc = new UserConnection();
		TouchUser user = userRepository.findOne(userId);
		uc.setUser(user);
		uc.setProviderId(data.getProviderId());
		uc.setProviderUserId(data.getProviderUserId());
		uc.setRank(rank==null?1:rank);
		uc.setDisplayName(data.getDisplayName());
		uc.setProfileUrl(data.getProfileUrl());
		uc.setImageUrl(data.getImageUrl());
		uc.setAccessToken(encrypt(data.getAccessToken()));
		uc.setSecret(encrypt(data.getSecret()));
		uc.setRefreshToken(encrypt(data.getRefreshToken()));
		uc.setExpireTime(data.getExpireTime());
		return uc;
	}
	
	private void updateUserConnectionContent(UserConnection userConnection, ConnectionData data){
		if(userConnection!=null && data!=null){
			userConnection.setDisplayName(data.getDisplayName());
			userConnection.setProfileUrl(data.getProfileUrl());
			userConnection.setImageUrl(data.getImageUrl());
			userConnection.setAccessToken(encrypt(data.getAccessToken()));
			userConnection.setSecret(encrypt(data.getSecret()));
			userConnection.setRefreshToken(encrypt(data.getRefreshToken()));
			userConnection.setExpireTime(data.getExpireTime());
		}
	}
	
	public void addConnection(Connection<?> connection) {
		try {
			ConnectionData data = connection.createData();
			Integer rank = userConnectionRepository.findNextRankByUserIdAndProviderId(userId, data.getProviderId());
			UserConnection userConnection = convert2UserConnection(rank, data);
			userConnection = userConnectionRepository.save(userConnection);			
		} catch (DuplicateKeyException e) {
			throw new DuplicateConnectionException(connection.getKey());
		}
	}
	
	public void updateConnection(Connection<?> connection) {
		ConnectionData data = connection.createData();
		Optional<UserConnection> cp = 
			userConnectionRepository.findByUserIdAndProviderIdAndProviderUserId(
				userId, data.getProviderId(), data.getProviderUserId()
			);
		if(cp.isPresent()){
			UserConnection userConnection = cp.get();
			updateUserConnectionContent(userConnection, data);
			userConnectionRepository.save(userConnection);
		}
	}

	public void removeConnections(String providerId) {
		userConnectionRepository.deleteByUserIdAndProviderId(userId, providerId);
//		userConnectionRepository.deleteByProviderId(providerId);
	}

	public void removeConnection(ConnectionKey connectionKey) {
		userConnectionRepository.deleteByUserIdAndProviderIdAndProviderUserId(
			userId, connectionKey.getProviderId(), connectionKey.getProviderUserId()
		);				
	}

	// internal helpers
	private Connection<?> findPrimaryConnection(String providerId) {
		Optional<ConnectionProperty> cps = userConnectionRepository.findTopByUserIdAndProviderIdOrderByRankAsc(userId, providerId);
		if(cps.isPresent()){
			return convert2Connection(cps.get());
		}else{
			return null;
		}	
	}
	
	private String decrypt(String encryptedText) {
		return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
	}
	private <A> String getProviderId(Class<A> apiType) {
		return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
	}	
	private String encrypt(String text) {
		return text != null ? textEncryptor.encrypt(text) : text;
	}

}
