/**
 * 
 */
package technology.touchmars.template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import technology.touchmars.template.model.UserConnectionWechat;

/**
 * @author jhcao
 *
 */
public interface UserConnectionWechatRepository extends JpaRepository<UserConnectionWechat, Long> {

	public Optional<UserConnectionWechat> findByAppIdAndTouchUserId(String appId, Long id);
	
	public Optional<UserConnectionWechat> findByAppIdAndTouchUserHashId(String appId, String hashId);
	
	public Optional<UserConnectionWechat> findByAppIdAndOpenId(String appId, String openId);
	
	public Long countByAppIdAndOpenId(String appId, String openId);
	
}
