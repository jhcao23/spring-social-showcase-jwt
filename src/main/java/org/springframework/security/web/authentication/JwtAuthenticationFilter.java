package org.springframework.security.web.authentication;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import technology.touchmars.template.service.JwtFullInfo;
import technology.touchmars.template.service.JwtTokenService;

public class JwtAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		Assert.isInstanceOf(HttpServletRequest.class, request, "request must be HttpServletRequest");
		Assert.isInstanceOf(HttpServletResponse.class, response, "response must be HttpServletResponse");
		
		if( setAuthenticationFromHeader((HttpServletRequest) request, (HttpServletResponse)response) ) {			
			chain.doFilter(request, response);
		}else {
			//JWT authentication failed then return! 
		}

	}

	private boolean setAuthenticationFromHeader(HttpServletRequest request, HttpServletResponse response) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication==null){
	        final String authHeader = request.getHeader(JwtTokenService.AUTHORIZATION);
	        if(authHeader!=null) {//from now on, confirm header has Authorization, then JWT authentication starts!
		        final String token = getTokenFromHeader(authHeader);
		        if(token!=null){	        	
		        		JwtFullInfo info = JwtTokenService.getFullInfo(token);
			        	String hashId = info.getHashId();
			        	Date expiry = info.getExpiry();
			        	Collection<GrantedAuthority> grantedAuthorityList = info.getGrantedAuthorityList();
			        	if(expiry!=null && expiry.getTime()>System.currentTimeMillis() && hashId!=null){
			        		SecurityContextHolder.getContext().setAuthentication(
			        			new UsernamePasswordAuthenticationToken(hashId, null, grantedAuthorityList)
			        		);
			        	}else{
			        		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			    			response.addHeader(JwtTokenService.AUTHORIZATION, JwtTokenService.AUTH_HEADER_REFRESH);
			    			return false; //only fail when [Authorization: Bearer $JWT] has token and fails!
			        	}
		        }
	        }
		}
		return true;
		
	}
	
	private String getTokenFromHeader(String authHeader){
		if(authHeader!=null && authHeader.trim().startsWith(JwtTokenService.BEARER)){
	        	String token = StringUtils.trimLeadingWhitespace(authHeader.substring(JwtTokenService.BEARER.length()));
	        	return token;
		}
		return null;
		
	}

}
