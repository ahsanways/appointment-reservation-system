package edu.miu.cs.cs544.dto;

import edu.miu.cs.cs544.domain.AppointmentStatus;
import edu.miu.cs.cs544.domain.Person;
import edu.miu.cs.cs544.domain.Session;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@ToString @Getter @Setter
public class AppointmentResponse {
    @NotNull
    @NonNull
    private int id;
    @NotNull
    @NonNull
    private AppointmentStatus status;
    @NotNull
    @NonNull
    private SessionResponse session;
    @NotNull
    @NonNull
    private PersonResponse customer;

    public void setCustomer(PersonResponse customer) {
        this.customer = customer;
    }

    public void setSession(SessionResponse session) {
        this.session = session;
    }
}
