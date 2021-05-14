package edu.miu.cs.cs544.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @RequiredArgsConstructor
public class Appointment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@NotNull
	@NonNull
	private AppointmentStatus status;
	@NotNull
	@NonNull
	@ManyToOne
	@JoinColumn(name="session_id")	
	private Session session;
	@NotNull
	@NonNull
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Person customer;
}
