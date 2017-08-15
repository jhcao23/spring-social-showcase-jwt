/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package technology.touchmars.template.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false, exclude="touchUser")
@Entity
public class Account extends LoginAccount {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private int id; 
	
	@Column(nullable = true, name="first_name")
	private String firstName;

	@Column(nullable = true, name="last_name")
	private String lastName;
	
	@OneToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(nullable=false, name="user_id")
	private TouchUser touchUser;

	public Account() {
		super();
	}

	public Account(String username, String password, String firstName, String lastName) {
		super();
		this.setUsername(username);
		this.setPassword(password);
		this.firstName = firstName;
		this.lastName = lastName;
	}

}
