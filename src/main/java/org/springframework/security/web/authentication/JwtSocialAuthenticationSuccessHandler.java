package org.springframework.security.web.authentication;

import static org.springframework.social.showcase.config.JwtTokenService.AUTH_HEADER_NAME;
import static org.springframework.social.showcase.config.JwtTokenService.getToken4User;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.JwtRedirectStrategy;
import org.springframework.social.showcase.account.User;
import org.springframework.social.showcase.account.UserRepository;
import org.springframework.util.StringUtils;

public class JwtSocialAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(JwtSocialAuthenticationSuccessHandler.class);
	
	private UserRepository userRepository;
	
	public JwtSocialAuthenticationSuccessHandler(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
		setRedirectStrategy(new JwtRedirectStrategy());
	}

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
		throws IOException, ServletException {
		
		logger.debug("JwtSocialAuthenticationSuccessHandler.onAuthenticationSuccess");
		logger.debug("request servlet path is {}",request.getServletPath());
		
		if(authentication!=null){
			String hashId = authentication.getName();
			logger.debug("hashId={}", hashId);
			if(!StringUtils.isEmpty(hashId)){
				Optional<User> optional = userRepository.findByHashId(hashId);
				if(optional.isPresent()){
					logger.debug("user is present!");
					User user = optional.get();
					String token = getToken4User(user);
					logger.debug("token is {} to add to header {}", token, AUTH_HEADER_NAME);
					response.addHeader(AUTH_HEADER_NAME, token);
//					response.addCookie(createCookieForToken(token));
				}
			}
		}
		//finally call super
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
//	private Cookie createCookieForToken(String token) {
//		final Cookie authCookie = new Cookie(AUTH_HEADER_NAME, token);
//		authCookie.setPath("/");
//		return authCookie;
//	}
}
