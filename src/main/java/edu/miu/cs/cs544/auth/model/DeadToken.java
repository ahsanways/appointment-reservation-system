package edu.miu.cs.cs544.auth.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor @Getter @Setter @RequiredArgsConstructor
public class DeadToken {
    @Id
    @GeneratedValue
    private int id;
    @NonNull
    @NotNull
    private String token;
    @NotNull
    @NonNull
    private long expirationTime;
}
