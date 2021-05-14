package edu.miu.cs.cs544.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.miu.cs.cs544.domain.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person,Integer>{
    @Query("from Person WHERE  username=:username ")
    Optional<Person> findByUsername(@Param("username") String username);    
    
    @Query("from Person WHERE  email=:email ")
    Optional<Person> findByEmail(@Param("email") String email);
}
