package technology.touchmars.showcase.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class UserConnection extends ConnectionAttributes {

	private Long id; 
	private TouchUser touchUser;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(nullable=false, name="user_id")	
	public TouchUser getTouchUser() {
		return touchUser;
	}
	public void setTouchUser(TouchUser user) {
		this.touchUser = user;
	}
	
	
}
