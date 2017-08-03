/**
 * 
 */
package org.springframework.security.web.authentication;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.social.showcase.model.User;
import org.springframework.social.showcase.repository.UserRepository;
import org.springframework.social.showcase.service.JwtTokenService;
import org.springframework.util.StringUtils;

/**
 * Serve for JwtUsernamePasswordAuthenticationFilter.
 * At the end of successfulAuthentication, call this handler's default method so that JWT is set in header.
 * @author jhcao
 *
 */
public class JwtAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private UserRepository userRepository;
	
	public JwtAuthenticationSuccessHandler(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}
	
	//we don't need redirect so no need for defaultTargetUrl
//	public JwtAuthenticationSuccessHandler(UserRepository userRepository, String defaultTargetUrl) {
//		super(defaultTargetUrl);
//		this.userRepository = userRepository;
//	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String username = authentication.getName();
		if(!authentication.isAuthenticated() || StringUtils.isEmpty(username))
			return ;
		Optional<User> optional = userRepository.findByHashId(username); 
		if(optional.isPresent()){
			User user = optional.get();
			String token = JwtTokenService.getToken4User(user);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.addHeader(JwtTokenService.AUTH_HEADER_NAME, token);
		}	
	}

}
