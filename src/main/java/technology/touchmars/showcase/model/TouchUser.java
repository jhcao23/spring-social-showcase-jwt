package technology.touchmars.showcase.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import technology.touchmars.showcase.service.GenerateUniqueKey;

@Data
@EqualsAndHashCode(callSuper=false, exclude="userConnectionWechatList")
@Entity(name="user")
public class TouchUser {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")	
	private Long id;
	
	@Column(name="hash_id", length=255)	
	private String hashId;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, optional=true, mappedBy="touchUser")		
	private Account account;
	
//	@ManyToMany(fetch=FetchType.EAGER)
//	@JoinTable(name="User_Authority", 
//		joinColumns=@JoinColumn(name="user_id", referencedColumnName="id"),
//		inverseJoinColumns=@JoinColumn(name="authority_id", referencedColumnName="id")
//	)
//	private Set<Authority> userAuthorityCollection = new HashSet<Authority>();

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = UserAuthority.class, mappedBy = "touchUser")
    private List<UserAuthority> userAuthorityCollection = new ArrayList<UserAuthority>();

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="touchUser")
	private Set<UserConnectionWechat> userConnectionWechatList = new HashSet<UserConnectionWechat>();
	
	@Transient
	public void assembleUser(String username, String password, String firstName, String lastName){
		Account a = new Account(username, password, firstName, lastName);
		a.setTouchUser(this);
		this.setAccount(a);
		this.setHashId(GenerateUniqueKey.getInstance().generateUniqueKeyUsingMessageDigest());
	}
	
	@Transient
	public void addAuthority(Authority authority) {	
		UserAuthority ua = new UserAuthority();
		ua.setTouchUser(this);
		ua.setAuthority(authority);
		this.userAuthorityCollection.add(ua);
	}
	
	@Transient
	public void addWechatConnection(UserConnectionWechat ucWechat) {
		if(ucWechat!=null) {
			ucWechat.setTouchUser(this);			
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
	@Transient
	public List<Authority> getAuthorityList(){
		return 
			this.getUserAuthorityCollection().stream().map(ua->ua.getAuthority())
				.collect(Collectors.toList());
	}
	
}
