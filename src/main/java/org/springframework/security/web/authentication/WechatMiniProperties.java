package org.springframework.security.web.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="wechat.mini")
public class WechatMiniProperties {

	private String appId;
	private String secret;
	
}
