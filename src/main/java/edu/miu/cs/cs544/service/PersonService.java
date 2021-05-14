package edu.miu.cs.cs544.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.cs.cs544.domain.Person;
import edu.miu.cs.cs544.domain.Role;
import edu.miu.cs.cs544.domain.RoleType;
import edu.miu.cs.cs544.repository.PersonRepository;
import edu.miu.cs.cs544.repository.RoleRepository;

@Service
public class PersonService implements IPersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RoleRepository roleRepository;

    // Util
    private boolean userNameTaken(String userName) {
        // Check if Person exists with the same username
        Optional<Person> sameUserNamePerson = personRepository.findByUsername(userName);
        if( sameUserNamePerson.isPresent() )
            throw new RuntimeException("User Name \""+userName+"\" is Already Taken By Another User!");
        return true;}
    // END Util

    @Override
    public Person createCustomer(Person person) {
        userNameTaken(person.getUsername());

        person.setPassword(new BCryptPasswordEncoder().encode(person.getPassword()));
        Role role = roleRepository.findByType(RoleType.CUSTOMER);
        person.addRole(role);
        return personRepository.save(person);
    }

    @Override
    public Person createCounselor(Person person) {
        userNameTaken(person.getUsername());

        person.setPassword(new BCryptPasswordEncoder().encode(person.getPassword()));
        Role role = roleRepository.findByType(RoleType.COUNSELOR);
        //role = roleRepository.save(role);
        person.addRole(role);
        return personRepository.save(person);
    }

    @Override
    public Person createAdmin(Person person) {
        userNameTaken(person.getUsername());

        person.setPassword(new BCryptPasswordEncoder().encode(person.getPassword()));
        Role role = roleRepository.findByType(RoleType.ADMIN);        
        person.addRole(role);
        return personRepository.save(person);
    }
    
    @Override
    public Person createPerson(Person person) {        
        person.setPassword(new BCryptPasswordEncoder().encode(person.getPassword()));        
        return personRepository.save(person);
    }

    @Override
    public Person getPerson() {
        return null;
    }
    
    @Override
    public Optional<Person> getPersonById(int id) {
        return personRepository.findById(id);
    }

    @Override
    public Person grantCounselorPrivilege(int personId) {
        Optional<Person> person = personRepository.findById(personId);
        Role counselorRole = roleRepository.findByType(RoleType.COUNSELOR);
        if(person.isPresent()){
            Person person1 = person.get();
            person1.addRole(counselorRole);
            return personRepository.save(person1);
        }
        return null;
    }

    @Override
    public Person revokeCounselorPrivilege(int personId) {
        Optional<Person> person = personRepository.findById(personId);
        Role counselorRole = roleRepository.findByType(RoleType.COUNSELOR);
        Role customerRole = roleRepository.findByType(RoleType.CUSTOMER);

        if(! person.isPresent()) throw new RuntimeException("Person is not found");

        if(person.isPresent()){
            Person person1 = person.get();
            person1.removeRole(counselorRole);
            if(person1.getRoles().isEmpty()) person1.addRole(customerRole);
            // TODO: 5/11/2021 when revoking counselor make sure to cancel future sessions!
            return personRepository.save(person1);
        }
        return null;
    }

    @Override
    public List<Person> getAllByRole(Role role) {
        return null;
    }

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Person giveRole(long personId, Role role) {
        return null;
    }

    @Override
    public Person removeRole(long personId, Role role) {
        return null;
    }

    @Override
    public void deletePerson(int personId) {
    	personRepository.deleteById(personId);
    }
    
    @Override
    public Person updatePerson(Person person) {
        personRepository.save(person);
        return person;
    }
    
    @Override
    public Optional<Person> getPersonByUsername(String username){
    	return personRepository.findByUsername(username);
    }
    @Override
    public Optional<Person> getPersonByEmail(String email){
    	return personRepository.findByEmail(email);
    }
}