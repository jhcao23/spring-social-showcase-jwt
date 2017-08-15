package technology.touchmars.template.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false) 
@Entity
@Table(name = "User_Authority")
public class UserAuthority {

    @Column(name = "id", nullable = false)
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")	
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, targetEntity = Authority.class)
    @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")
    private Authority authority;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, targetEntity = TouchUser.class)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private TouchUser touchUser;

}
