package org.springframework.social.showcase.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.springframework.social.showcase.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenService {

	public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
	public static final String AUTH_HEADER_REFRESH = "refresh";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	
	public static final String SECRET_KEY = "secretKey";
	
	public static String getToken4User(User user){
		return 
			Jwts.builder().setSubject(user.getHashId())
				.claim("roles", "user")
				.setIssuedAt(Calendar.getInstance().getTime())
				.setExpiration(Date.from(LocalDateTime.now().plusDays(10).atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
		
	}
	
	public static String getHashId(String token){
		if(token!=null){
			try{
				Claims claim = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJwt(token).getBody();
				return claim.getSubject();
			}catch (Exception e) {
				e.printStackTrace();				
			}
		}
		return null;
	}

	
}
