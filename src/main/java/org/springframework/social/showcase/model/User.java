package org.springframework.social.showcase.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.social.showcase.service.GenerateUniqueKey;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false, exclude="userConnectionWechatList")
@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")	
	private Long id;
	
	@Column(name="hash_id", length=255)	
	private String hashId;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY, optional=true, mappedBy="user")		
	private Account account;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="User_Authority", 
		joinColumns=@JoinColumn(name="user_id", referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="authority_id", referencedColumnName="id")
	)
	private Set<Authority> authorityList = new HashSet<Authority>();

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	private Set<UserConnectionWechat> userConnectionWechatList = new HashSet<UserConnectionWechat>();
	
	@Transient
	public void assembleUser(String username, String password, String firstName, String lastName){
		Account a = new Account(username, password, firstName, lastName);
		a.setUser(this);
		this.setAccount(a);
		this.setHashId(GenerateUniqueKey.getInstance().generateUniqueKeyUsingMessageDigest());
	}
	
	@Transient
	public void addAuthority(Authority authority) {		
		this.authorityList.add(authority);
	}
	
	@Transient
	public void addWechatConnection(UserConnectionWechat ucWechat) {
		if(ucWechat!=null) {
			ucWechat.setUser(this);			
			this.userConnectionWechatList.add(ucWechat);
		}
	}	
	@Transient 
	public void createUserWithWechatConnection(String appId, String openId, String unionId, String sessionKey, Long expires) {
		UserConnectionWechat ucWechat = new UserConnectionWechat();
		ucWechat.setAppId(appId);
		ucWechat.setOpenId(openId);
		ucWechat.setUnionId(unionId);
		ucWechat.setSessionKey(sessionKey);
		ucWechat.setExpires(expires);
		this.setHashId(GenerateUniqueKey.getInstance().generateUniqueKeyUsingMessageDigest());
		this.addWechatConnection(ucWechat);
	}
	
}
