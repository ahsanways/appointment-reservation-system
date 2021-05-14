package edu.miu.cs.cs544.dto;

import edu.miu.cs.cs544.domain.Role;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@ToString @Setter @Getter
public class PersonRequest {
    @Length(max = 50)
    private String firstName;
    @NotNull
    @NonNull
    @Length(max = 50)
    private String lastName;
    @Email
    @Length(max = 254)
    private String email;
    @NotNull
    @NonNull
    @Length(max = 50)
    private String username;
    private String password;
}
