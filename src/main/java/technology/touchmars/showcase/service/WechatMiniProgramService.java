package technology.touchmars.showcase.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class WechatMiniProgramService {

	private static final Map<String, String> mapAppId = new HashMap<String, String>();
	private static final Map<String, String> mapSecret = new HashMap<String, String>();
	
	{
		mapAppId.put("e2eat", "wxc9213fff88a427f0");
		mapSecret.put("e2eat", "2e6bc5e65e470acf77d545d72ad61d4a");
	}
	
	public String getAppId(String app) {
		return mapAppId.get(app);
	}
	
	public String getSecret(String app) {
		return mapSecret.get(app);
	}

}
