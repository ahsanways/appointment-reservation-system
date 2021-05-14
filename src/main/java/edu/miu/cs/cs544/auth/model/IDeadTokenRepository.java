package edu.miu.cs.cs544.auth.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface IDeadTokenRepository extends JpaRepository<DeadToken,Integer> {
    @Query("from DeadToken where token=:token")
    Optional<DeadToken> findDeadToken(@Param("token") String token);

    @Modifying
    @Query("DELETE from DeadToken where expirationTime < :time")
    void cleanDeadTokenBin(@Param("time") long time);
}
