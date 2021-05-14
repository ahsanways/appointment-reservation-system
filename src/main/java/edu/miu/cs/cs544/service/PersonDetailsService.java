package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.Person;
import edu.miu.cs.cs544.domain.RoleType;
import edu.miu.cs.cs544.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class PersonDetailsService implements UserDetailsService {
    @Autowired
    private PersonRepository personRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username).get();
        if(person == null){
            throw new UsernameNotFoundException("username " + username + " not found!");
        }
        return new User(person.getUsername(), person.getPassword(), getGrantedAuthority(person));
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthority(Person person) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        person.getRoles().stream().map(role -> role.getRoleType()).forEach(roleType -> authorities.add(new SimpleGrantedAuthority(roleType.getAuthority())));
        return authorities;
    }

    public Person getCurrentPerson(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        return personRepository.findByUsername(username).get();
    }
    
    public Person getPersonById(int personId){
        return personRepository.findById(personId).get();
    }
}
