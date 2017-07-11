package org.springframework.security.web.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.showcase.config.JwtTokenService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		Assert.isInstanceOf(HttpServletRequest.class, request, "request must be HttpServletRequest");
		Assert.isInstanceOf(HttpServletResponse.class, response, "response must be HttpServletResponse");
		
		setAuthenticationFromHeader((HttpServletRequest) request, (HttpServletResponse)response);
		chain.doFilter(request, response);

	}

	private void setAuthenticationFromHeader(HttpServletRequest request, HttpServletResponse response) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication==null){
	        final String authHeader = request.getHeader(JwtTokenService.AUTHORIZATION);
	        final String token = getTokenFromHeader(authHeader);
	        if(token!=null){
	        	String hashId = JwtTokenService.getHashId(token);
	        	if(hashId!=null){
	        		SecurityContextHolder.getContext().setAuthentication(
	        			new UsernamePasswordAuthenticationToken(hashId, null, null)
	        		);
	        	}else{
	        		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        		response.addHeader(JwtTokenService.AUTHORIZATION, JwtTokenService.AUTH_HEADER_REFRESH);;
	        	}
	        }
		}
		
	}
	
	private String getTokenFromHeader(String authHeader){
		if(authHeader!=null && authHeader.trim().startsWith(JwtTokenService.BEARER)){
        	String token = StringUtils.trimLeadingWhitespace(authHeader.substring(JwtTokenService.BEARER.length()));
        	return token;
		}
		return null;
		
	}

}
