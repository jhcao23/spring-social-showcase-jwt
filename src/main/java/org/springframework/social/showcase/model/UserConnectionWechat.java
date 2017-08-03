package org.springframework.social.showcase.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false, exclude="user")
@Entity
public class UserConnectionWechat {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id; 
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(nullable=false, name="user_id")	
	private User user;
	
	@Column(name="app_id")
	private String appId;
	
	@Column(name="subscribe")
	private Boolean subscribe;
	
	@Column(name="open_id")
	private String openId;
	
	@Column(name="union_id")
	private String unionId;
	
	@Column(name="session_key")
	private String sessionKey;
	
	@Column(name="expires")
	private Long expires;
	
	@Column(name="nickname")
	private String nickname;
	
	@Column(name="sex")
	private Integer sex; //值为1时是男性，值为2时是女性，值为0时是未知
	
	@Column(name="city")
	private String city;
	
	@Column(name="province")
	private String province;
	
	@Column(name="country")
	private String country;
	
	@Column(name="language")
	private String language;
	
	@Column(name="avatar_url")
	private String avatarUrl;
	
	@Column(name="subscribe_time")
	private Long subscribeTime;
	
}
