package edu.miu.cs.cs544.repository;

import java.util.List;
import java.util.Optional;

import edu.miu.cs.cs544.domain.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.cs.cs544.domain.Appointment;


@Repository
@Transactional
public interface AppointmentRepository extends JpaRepository<Appointment,Integer>{

    @Query("from Appointment WHERE session.id=:session_id and customer.id=:customer_id")
    Optional<Appointment> findBySessionCustomer(@Param("session_id") int session_id,
    		                                    @Param("customer_id") int customer_id);

    @Query("select a from Appointment a where a.customer.id = :customer_id")
    List<Appointment> findByCustomerId(@Param("customer_id") int customer_id);

    @Query("select a from Appointment a where a.session.id = :session_id")
    List<Appointment> findBySessionId(@Param("session_id") int session_id);

    @Query("select a from Appointment a where a.status = :appointment_status and a.session.id = :session_id")
    List<Appointment> findByStatusAndSessionId(@Param("appointment_status") AppointmentStatus appointment_status, @Param("session_id") int session_id);
}
