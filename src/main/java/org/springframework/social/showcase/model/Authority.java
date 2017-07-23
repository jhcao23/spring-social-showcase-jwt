/**
 * 
 */
package org.springframework.social.showcase.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NaturalId;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jhcao
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity(name="Authority")
@Immutable
public class Authority extends CommonEntity {

	public static final int ID_ROLE_USER = 1;
	public static final int ID_ROLE_ADMIN = 2;
	
	@Id
	@NaturalId
	private Integer id;
	
	@Column(unique = true, nullable = false, name="authority_name")
	private String authorityName;
	
	@Column(name="description")
	private String description;
	
}
