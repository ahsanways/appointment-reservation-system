package edu.miu.cs.cs544.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Session {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
//	@Future
	@NotNull
	@NonNull
	private LocalDate date;
	@NotNull
	@NonNull
	private LocalTime startTime;
	@NotNull
	@NonNull
	private int duration;
	@NotNull
	@NonNull
	@Length(max = 100)
	private String location;
	@ManyToOne
	@JoinColumn(name="counselor_id")
	private Person counselor;	

	@OneToMany(mappedBy="session",cascade= CascadeType.ALL)
	@JsonIgnore
	private List<Appointment> appointments = new ArrayList<>();
	
	public void addAppointment(Appointment a) {
		a.setSession(this);
		this.appointments.add(a);
	}
	
	public void removeAppointment(Appointment a) {
		this.appointments.remove(a);
	}

	public boolean isFutureSession(){
		return getDate().compareTo(LocalDate.now()) > 0;
	}
}
