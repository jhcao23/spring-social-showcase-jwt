package technology.touchmars.template.service;

import org.springframework.core.env.Environment;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class WechatMiniDiscoveryServiceImpl implements WechatMiniProgramDiscoveryService {

	public static final String DEFAULT_PREFIX = "wechat.mini";
	public static final String DEFAULT_POSTFIX_APP_ID = "app-id";
	public static final String DEFAULT_POSTFIX_SECRET = "secret";
	
	private String prefix = DEFAULT_PREFIX;
	@NonNull private Environment environment;
	
	@Override
	public String getAppId(String appName) {		
		return environment.getProperty(prefix+"."+appName+"."+DEFAULT_POSTFIX_APP_ID);
	}

	@Override
	public String getSecret(String appName) {
		return environment.getProperty(prefix+"."+appName+"."+DEFAULT_POSTFIX_SECRET);
	}

}
