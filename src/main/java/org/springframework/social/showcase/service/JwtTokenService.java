package org.springframework.social.showcase.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.social.showcase.model.Authority;
import org.springframework.social.showcase.model.User;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenService {

	public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
	public static final String AUTH_HEADER_REFRESH = "refresh";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	
	public static final String SECRET_KEY = "secretKey";
	private static final String AUTHORITIES_KEY = "auth";
	
	public static String getToken4User(User user){
		String authorities = user.getAuthorityList().stream()
	            .map(Authority::getAuthorityName)
	            .collect(Collectors.joining(","));
		if(!StringUtils.hasText(authorities))
			authorities = "ROLE_USER";
		String result = 
			Jwts.builder()
				.setSubject(user.getHashId())
				.claim(AUTHORITIES_KEY, authorities)
				.setIssuedAt(Calendar.getInstance().getTime())
				.setExpiration(Date.from(LocalDateTime.now().plusDays(10).atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
		try {
			String subject =
				Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(result).getBody().getSubject();
			if(!subject.equals(user.getHashId())) {
				System.err.println(String.format("SHIT! %s != %s", user.getHashId(), subject));
			}
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return result;
		
	}
	
	public static String getHashId(String token){
		if(token!=null){
			try{
				Claims claim = 
					Jwts
						.parser()						
						.setSigningKey(SECRET_KEY)
						.parseClaimsJws(token)
						.getBody()
					;
				return claim.getSubject();
			}catch (Exception e) {
				e.printStackTrace();				
			}
		}
		return null;
	}

	
}
