package edu.miu.cs.cs544.dto;

import edu.miu.cs.cs544.domain.Role;
import edu.miu.cs.cs544.domain.RoleType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ToString @Setter @Getter
public class PersonResponse {
    @NotNull
    @NonNull
    private int id;
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
    private Set<RoleType> roles = new HashSet<>();

    public void setRoles(Set<Role> roles){
        this.roles = roles.stream().map(Role::getRoleType).collect(Collectors.toSet());
    }
}
