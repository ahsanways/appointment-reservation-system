package edu.miu.cs.cs544.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SecondaryTable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@SecondaryTable(name="PersonCredentials")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter 
public class Person {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@NotNull
	@NonNull
	@Length(max = 50)
	private String firstName;
	@NotNull
	@NonNull
	@Length(max = 50)
	private String lastName;
	@Email
	@Length(max = 254)
	private String email;
	@Column(table = "PersonCredentials",unique = true )
	@NotNull
	@NonNull
	@Length(max = 50)
	private String username;
	@Column(table = "PersonCredentials")
	@NotNull
	@NonNull
	@Length(max = 100)
	private String password;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "person_role",
		joinColumns = @JoinColumn(name="person_id"),
		inverseJoinColumns = @JoinColumn(name="role_id")
	)
	private Set<Role> roles = new HashSet<Role>();

	public void addRole(Role role) {		
		roles.add(role);
	}
	public void removeRole(Role role){
		this.roles.remove(role);
	}
}
