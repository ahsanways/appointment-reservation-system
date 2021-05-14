package edu.miu.cs.cs544.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor @Getter @Setter
public class Role {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Setter(AccessLevel.PRIVATE) private int id;
	
	@Column(name="role_type", unique=true)
	@NonNull
	@Enumerated(EnumType.STRING)
	private RoleType roleType;
}
