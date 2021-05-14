package edu.miu.cs.cs544.service;



import java.util.List;
import java.util.Optional;

import edu.miu.cs.cs544.domain.Person;
import edu.miu.cs.cs544.domain.Role;

public interface IPersonService {
    Person createCustomer(Person person);
    Person createCounselor(Person person);
    Person createAdmin(Person person);
    Person createPerson(Person person);
    Person getPerson();
    List<Person> getAllByRole(Role role);
    List<Person> getAll();
    Person giveRole(long personId,Role role);
    Person removeRole(long personId, Role role);
    void deletePerson(int personId);
    Person updatePerson(Person person);
    Optional<Person> getPersonById(int id);
    Optional<Person> getPersonByUsername(String username);
    Optional<Person> getPersonByEmail(String email);
    Person grantCounselorPrivilege(int personId);
    Person revokeCounselorPrivilege(int personId);
}
