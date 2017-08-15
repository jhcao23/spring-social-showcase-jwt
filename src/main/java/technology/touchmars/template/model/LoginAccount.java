package technology.touchmars.template.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@MappedSuperclass
public class LoginAccount {

	@Column(unique = true, nullable = false, name="username")
	private String username;
	@Column(nullable = false)
	private String password;

}
