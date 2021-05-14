package edu.miu.cs.cs544.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.miu.cs.cs544.domain.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface SessionRepository extends JpaRepository<Session,Integer>{
    @Query("select s from Session  s where  s.counselor.id = :counselor_id")
    List<Session> findSessionsByCounselorId(@Param("counselor_id") int counselor_id);

}
