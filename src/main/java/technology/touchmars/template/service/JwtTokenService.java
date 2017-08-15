package technology.touchmars.template.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import technology.touchmars.template.model.Authority;
import technology.touchmars.template.model.TouchUser;

public class JwtTokenService {

	public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
	public static final String AUTH_HEADER_REFRESH = "refresh";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	
	public static final String SECRET_KEY = "secretKey";
	private static final String AUTHORITIES_KEY = "auth";
	
	public static String getToken4User(String userHashId, Collection<Authority> authorityList, Long expires) {
		String authorities = authorityList.stream()
	            .map(Authority::getName)
	            .collect(Collectors.joining(","));
		if(!StringUtils.hasText(authorities))
			authorities = "ROLE_USER";
		if(expires==null) {
			expires = System.currentTimeMillis() + 1000*7200;
		}
		String result = 
			Jwts.builder()
					.setSubject(userHashId)
					.claim(AUTHORITIES_KEY, authorities)
					.setIssuedAt(Calendar.getInstance().getTime())
					.setExpiration(new Date(expires))
					.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
					.compact();
		try {
			String subject =
				Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(result).getBody().getSubject();
			if(!subject.equals(userHashId)) {
				System.err.println(String.format("SHIT! %s != %s", userHashId, subject));
			}
		}catch (Exception e) {
			e.printStackTrace();
			
		}
			
		return result;
	}
	
	public static String getToken4User(TouchUser user){
		long expiry = Date.from(LocalDateTime.now().plusDays(10).atZone(ZoneId.systemDefault()).toInstant()).getTime();
		return getToken4User(user.getHashId(), user.getAuthorityList(), expiry);
//		String authorities = user.getAuthorityList().stream()
//	            .map(Authority::getName)
//	            .collect(Collectors.joining(","));
//		if(!StringUtils.hasText(authorities))
//			authorities = "ROLE_USER";
//		String result = 
//			Jwts.builder()
//				.setSubject(user.getHashId())
//				.claim(AUTHORITIES_KEY, authorities)
//				.setIssuedAt(Calendar.getInstance().getTime())
//				.setExpiration(Date.from(LocalDateTime.now().plusDays(10).atZone(ZoneId.systemDefault()).toInstant()))	//TODO: Default 10 days
//				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//				.compact();
//		try {
//			String subject =
//				Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(result).getBody().getSubject();
//			if(!subject.equals(user.getHashId())) {
//				System.err.println(String.format("SHIT! %s != %s", user.getHashId(), subject));
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			
//		}
//		
//		return result;
		
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
	
	public static Collection<GrantedAuthority> getGrantedAuthorityList(Claims claim){
		String authorities = claim.get(AUTHORITIES_KEY, String.class);
		return Stream.of(authorities.split("\\s*,\\s*"))
					.map(a->new SimpleGrantedAuthority(a))
					.collect(Collectors.toList());
	}
	
	public static Collection<GrantedAuthority> getGrantedAuthorityList(String token){
		if(token!=null){
			try{
				Claims claim = 
					Jwts
						.parser()						
						.setSigningKey(SECRET_KEY)
						.parseClaimsJws(token)
						.getBody()
					;
				return getGrantedAuthorityList(claim);
			}catch (Exception e) {
				e.printStackTrace();				
			}
		}
		return null;
	}
	
	public static String getAuthorities(String token){
		if(token!=null){
			try{
				Claims claim = 
					Jwts
						.parser()						
						.setSigningKey(SECRET_KEY)
						.parseClaimsJws(token)
						.getBody()
					;
				String authorities = claim.get(AUTHORITIES_KEY, String.class);
				return authorities;
			}catch (Exception e) {
				e.printStackTrace();				
			}
		}
		return null;
	}
	
	public static Date getExpiration(String token){
		if(token!=null){
			try{
				Claims claim = 
					Jwts
						.parser()						
						.setSigningKey(SECRET_KEY)
						.parseClaimsJws(token)
						.getBody()
					;
				return claim.getExpiration();
			}catch (Exception e) {
				e.printStackTrace();				
			}
		}
		return null;
	}
	
	public static JwtFullInfo getFullInfo(String token){
		if(token!=null){
			try{
				Claims claim = 
					Jwts
						.parser()						
						.setSigningKey(SECRET_KEY)
						.parseClaimsJws(token)
						.getBody()
					;
				Date expiry = claim.getExpiration();
				String hashId = claim.getSubject();
				Collection<GrantedAuthority> grantedAuthorityList = getGrantedAuthorityList(claim);
				return new JwtFullInfo(hashId, expiry, grantedAuthorityList);
			}catch (Exception e) {
				e.printStackTrace();				
			}
		}
		return null;
		
	}

	
}
