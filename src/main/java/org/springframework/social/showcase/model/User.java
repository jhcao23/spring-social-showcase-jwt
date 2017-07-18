package org.springframework.social.showcase.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.social.showcase.service.GenerateUniqueKey;

@Entity
public class User {

	private Integer id;
	private String hashId;
	private Account account;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="hash_id", length=512)
	public String getHashId() {
		return hashId;
	}
	public void setHashId(String hashId) {
		this.hashId = hashId;
	}

	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY, optional=true, mappedBy="user")	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
	@Transient
	public void assembleUser(String username, String password, String firstName, String lastName){
		Account a = new Account(username, password, firstName, lastName);
		a.setUser(this);
		this.setAccount(a);
		this.setHashId(GenerateUniqueKey.getInstance().generateUniqueKeyUsingMessageDigest());
	}
	
}
