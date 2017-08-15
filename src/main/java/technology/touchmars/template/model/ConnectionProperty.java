package technology.touchmars.template.model;

import org.springframework.beans.factory.annotation.Value;

public interface ConnectionProperty {

	 @Value("#{target.providerId}")
	 public String getProviderId();
	 
	 @Value("#{target.providerUserId}")
	 public String getProviderUserId();
	 
	 @Value("#{target.rank}")
	 public Integer getRank();	
	 
	 @Value("#{target.displayName}")
	 public String getDisplayName();	
	 
	 @Value("#{target.profileUrl}")
	 public String getProfileUrl ();
	 
	 @Value("#{target.imageUrl}")
	 public String getImageUrl ();
	 
	 @Value("#{target.accessToken}")
	 public String getAccessToken ();
	 
	 @Value("#{target.secret}")
	 public String getSecret ();
	 
	 @Value("#{target.refreshToken}")
	 public String getRefreshToken ();
	 
	 @Value("#{target.expireTime}")
	 public Long getExpireTime ();
	 
}
