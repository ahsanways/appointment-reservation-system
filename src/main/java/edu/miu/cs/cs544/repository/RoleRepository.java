package edu.miu.cs.cs544.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.cs.cs544.domain.Role;
import edu.miu.cs.cs544.domain.RoleType;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role,Integer>{
    @Query("from Role WHERE  roleType=:roleType ")
    Role findByType(@Param("roleType") RoleType roleType);
}
