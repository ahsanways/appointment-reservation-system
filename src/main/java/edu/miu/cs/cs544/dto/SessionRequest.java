package edu.miu.cs.cs544.dto;

import edu.miu.cs.cs544.domain.Person;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@ToString @Getter @Setter
public class SessionRequest {
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
}
