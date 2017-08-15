package org.springframework.security.web.authentication;

public interface WechatMiniDiscoveryService {

	public String getAppId(String appName);
	public String getSecret(String appName);
	
}
