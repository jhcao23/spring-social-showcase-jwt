package org.springframework.security.web.authentication;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import technology.touchmars.feign.wechat.client.api.MiniProgramUnionApiClient;
import technology.touchmars.feign.wechat.client.config.WechatClientException;
import technology.touchmars.feign.wechat.client.model.SessionKeyToken;
import technology.touchmars.template.model.TouchUser;
import technology.touchmars.template.model.UserConnectionWechat;
import technology.touchmars.template.repository.AuthorityRepository;
import technology.touchmars.template.repository.UserConnectionWechatRepository;
import technology.touchmars.template.repository.UserRepository;
import technology.touchmars.template.service.JpaConnectionSignUp;
import technology.touchmars.template.service.WechatMiniProgramDiscoveryService;

/**
 * This is the API|URL for login requested from wechat mini program.
 * 
 *  mini program will send request to default but customizable URL {@code url} or {@value DEFAULT_REST_LOGIN_URL},
 *  this filter will pull out the {@code code} from the request and then exchange the request to wechat server
 *  with following API: {@link https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code}.
 *  
 *  It will return openid & session_key & unionid.
 * 
 * Learn from UsernamePasswordAuthenticationFilter from Spring Security,
 * plus override AbstractAuthenticationProcessingFilter.successfulAuthentication.
 *  
 * @author jhcao
 *
 */
public class WechatMiniAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	public static final String DEFAULT_REST_LOGIN_URL = "/rest/mini/signin";
	
	private UserRepository userRepository;
	private AuthorityRepository authorityRepository;
	private UserConnectionWechatRepository userConnectionWechatRepository;
	private MiniProgramUnionApiClient miniProgramUnionApiClient;
	private WechatMiniProgramDiscoveryService wechatMiniProgramDiscoveryService;
	private boolean postOnly = true;	
	
	public WechatMiniAuthenticationFilter(
		WechatMiniProgramDiscoveryService wechatMiniProgramDiscoveryService,
		MiniProgramUnionApiClient miniProgramUnionApiClient, 
		UserRepository userRepository, 
		UserConnectionWechatRepository connectionWechatRepository, 
		AuthorityRepository authorityRepository, 
		String url) {
		super(new AntPathRequestMatcher(StringUtils.hasText(url)?url:DEFAULT_REST_LOGIN_URL, "POST"));
		this.miniProgramUnionApiClient = miniProgramUnionApiClient;
		this.wechatMiniProgramDiscoveryService = wechatMiniProgramDiscoveryService;
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
		this.userConnectionWechatRepository = connectionWechatRepository;
		this.setAuthenticationFailureHandler(new WechatMiniFailureHandler());
		this.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler(userRepository));
	}

	//TODO: consider to use super.successfulAuthentication (remove this method)
	//but set a different successHandler called JwtAuthenticationSuccessHandler?
//	@Override
//	protected void successfulAuthentication(HttpServletRequest request,
//			HttpServletResponse response, FilterChain chain, Authentication authResult)
//			throws IOException, ServletException {
//		String hashId = authResult.getName();
//		if(StringUtils.isEmpty(hashId))
//			return ;
//		Optional<User> optional = userRepository.findByHashId(hashId); 
//		if(optional.isPresent()){
//			User user = optional.get();
//			String token = JwtTokenService.getToken4User(user);
//			response.setContentType("application/json");
//			response.addHeader(JwtTokenService.AUTH_HEADER_NAME, token);
//		}		
//		//hello don't call super, we just want to set HEADER; otherwise super will redirect
////		super.successfulAuthentication(request, response, chain, authResult);
//	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		if (postOnly && !request.getMethod().trim().equalsIgnoreCase(HttpMethod.POST.name())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		String code = null, program = null, username = null;
		if(request.getMethod().trim().equalsIgnoreCase("POST")) {
			String contentType = request.getContentType().toLowerCase();
			logger.debug("contentType is {}", contentType);
			
			if(contentType.contains("json")) {
				ProgramCode programCode = new ObjectMapper().readValue(request.getReader(), ProgramCode.class);				
				code = programCode.getCode();
				program = programCode.getProgram();
				String appId = wechatMiniProgramDiscoveryService.getAppId(program);
				String secret = wechatMiniProgramDiscoveryService.getSecret(program);
				SessionKeyToken skToken = null;
				long timestamp = System.currentTimeMillis();				
				try {
					ResponseEntity<SessionKeyToken> skTokenResponse = miniProgramUnionApiClient.exchangeCode(appId, secret, code, "authorization_code");
					skToken = skTokenResponse.getBody();						
				}catch(WechatClientException e) {
					e.printStackTrace();
				}catch(Exception e) {
					e.printStackTrace();
				}
				if(skToken!=null) {
					logger.debug("got a session key token: {}", skToken);
					Integer expiresIn = skToken.getExpiresIn();
					if(expiresIn!=null) {
						timestamp += expiresIn*1000;
					}else {
						timestamp += 7200*1000;	//Default expires_in 7200 seconds
					}
					String openId = skToken.getOpenId();
					String unionId = skToken.getUnionId();
					String sessionKey = skToken.getSessionKey();
					Optional<UserConnectionWechat> ucWechatO = userConnectionWechatRepository.findByAppIdAndOpenId(appId, openId);
					TouchUser user = null;
					if(!ucWechatO.isPresent()) {//create a User with new UserConnectionWechat
						TouchUser user0 = JpaConnectionSignUp.createUser4Connection(authorityRepository);					
						user0.createUserWithWechatConnection(appId, openId, unionId, sessionKey, timestamp);
						user = userRepository.save(user0);
						
					}else {//existing User simply login
						UserConnectionWechat ucWechat = ucWechatO.get();
						//update sessionKey
						ucWechat.setSessionKey(sessionKey);
						ucWechat.setExpires(timestamp);
						ucWechat = userConnectionWechatRepository.save(ucWechat);
						user = ucWechat.getTouchUser();						
					}	
					if(user!=null) {
						username = user.getHashId();
						username = username.trim();
						if (!StringUtils.hasText(username)) {
							username = "";
						}
						//add Authority
						List<GrantedAuthority> grantedAuthorities = 
							user.getAuthorityList()
							.stream()
							.map(
								a->new SimpleGrantedAuthority(a.getName())).collect(Collectors.toList()
							);
						response.setHeader(JwtAuthenticationSuccessHandler.EXPIRE, ""+timestamp);
						UsernamePasswordAuthenticationToken authToken = 
								new UsernamePasswordAuthenticationToken(username, "", grantedAuthorities);	
						//TODO: let's make it simple, simply return an authenticated one so bypass getAuthenticationManager().authenticate!
	//					return this.getAuthenticationManager().authenticate(authToken);
						return authToken;
					}
				}
			}			
			
		}
		throw new AuthenticationCredentialsNotFoundException("code not valid");
		
	}
	
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}
	
}

@Data
class ProgramCode {
	private String code;
	private String program;		
}
