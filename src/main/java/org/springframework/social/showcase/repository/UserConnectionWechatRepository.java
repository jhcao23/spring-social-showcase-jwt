/**
 * 
 */
package org.springframework.social.showcase.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.social.showcase.model.UserConnectionWechat;

/**
 * @author jhcao
 *
 */
public interface UserConnectionWechatRepository extends JpaRepository<UserConnectionWechat, Long> {

	public Optional<UserConnectionWechat> findByAppIdAndUserId(String appId, Long id);
	
	public Optional<UserConnectionWechat> findByAppIdAndUserHashId(String appId, String hashId);
	
	public Optional<UserConnectionWechat> findByAppIdAndOpenId(String appId, String openId);
	
	public Long countByAppIdAndOpenId(String appId, String openId);
	
}
