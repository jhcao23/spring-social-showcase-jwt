package org.springframework.security.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JwtRedirectStrategy extends DefaultRedirectStrategy{
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		
		String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
		redirectUrl = response.encodeRedirectURL(redirectUrl);

		if (logger.isDebugEnabled()) {
			logger.debug("Browser should be Redirecting to '" + redirectUrl + "'");
		}
		
		//JWT Strategy doesn't like redirect otherwise JS won't catch the JWT header
		//simply ask browser do redirect by itself
//		response.sendRedirect(redirectUrl);
		response.addHeader("redirect", redirectUrl);
	}
}
