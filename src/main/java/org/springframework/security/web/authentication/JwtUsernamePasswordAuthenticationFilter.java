package org.springframework.security.web.authentication;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import technology.touchmars.template.model.LoginAccount;
import technology.touchmars.template.repository.UserRepository;

/**
 * Learn from UsernamePasswordAuthenticationFilter from Spring Security,
 * plus override AbstractAuthenticationProcessingFilter.successfulAuthentication.
 *  
 * @author jhcao
 *
 */
public class JwtUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
	
	public static final String DEFAULT_REST_LOGIN_URL = "/rest/signin";
	public static final String DEFAULT_WEB_LOGIN_URL = "/signin/authenticate";
	
//	private UserRepository userRepository;
	private boolean postOnly = true;	
	
	public JwtUsernamePasswordAuthenticationFilter(UserRepository userRepository) {
		super(
			new OrRequestMatcher(
				new AntPathRequestMatcher(DEFAULT_REST_LOGIN_URL, "POST"),
				new AntPathRequestMatcher(DEFAULT_WEB_LOGIN_URL, "POST")
			)
		);
//		this.userRepository = userRepository;
		this.setAuthenticationSuccessHandler((new JwtAuthenticationSuccessHandler(userRepository)));
	}

	//TODO: consider to use super.successfulAuthentication (remove this method)
	//but set a different successHandler called JwtAuthenticationSuccessHandler?
//	@Override
//	protected void successfulAuthentication(HttpServletRequest request,
//			HttpServletResponse response, FilterChain chain, Authentication authResult)
//			throws IOException, ServletException {
//		String username = authResult.getName();
//		if(StringUtils.isEmpty(username))
//			return ;
//		Optional<User> optional = userRepository.findByHashId(username); 
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
		if (postOnly && !request.getMethod().trim().equalsIgnoreCase("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}
		String username = null, password = null;
		if(request.getMethod().trim().equalsIgnoreCase("POST")) {
			String contentType = request.getContentType().toLowerCase();				
			if(contentType.contains("json")) {
				LoginAccount account = new ObjectMapper().readValue(request.getReader(), LoginAccount.class);				
				username = account.getUsername();		
				password = account.getPassword();
			}else {
				username = request.getParameter(usernameParameter);
				password = request.getParameter(passwordParameter);
				
			}
			if (username == null) {
				username = "";
			}
			if (password == null) {
				password = "";
			}
			username = username.trim();
		}
		UsernamePasswordAuthenticationToken authRequest = 
			new UsernamePasswordAuthenticationToken(username, password);
		
		// Allow subclasses to set the "details" property
//		setDetails(request, authRequest);
		
		return this.getAuthenticationManager().authenticate(authRequest);
	}
	
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	/**
	 * Sets the parameter name which will be used to obtain the username from the login
	 * request.
	 *
	 * @param usernameParameter the parameter name. Defaults to "username".
	 */
	public void setUsernameParameter(String usernameParameter) {
		Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
		this.usernameParameter = usernameParameter;
	}

	/**
	 * Sets the parameter name which will be used to obtain the password from the login
	 * request..
	 *
	 * @param passwordParameter the parameter name. Defaults to "password".
	 */
	public void setPasswordParameter(String passwordParameter) {
		Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
		this.passwordParameter = passwordParameter;
	}

	public final String getUsernameParameter() {
		return usernameParameter;
	}

	public final String getPasswordParameter() {
		return passwordParameter;
	}
}
