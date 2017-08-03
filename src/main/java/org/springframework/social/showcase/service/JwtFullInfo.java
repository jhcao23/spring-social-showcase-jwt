package org.springframework.social.showcase.service;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtFullInfo {

	private String hashId;
	private Date expiry;
	private Collection<GrantedAuthority> grantedAuthorityList;

}
