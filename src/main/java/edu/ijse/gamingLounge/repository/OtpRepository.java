package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

    @Query("SELECT o FROM Otp o WHERE o.customer.id = :customerId ORDER BY o.id DESC LIMIT 1")
    Optional<Otp> findLatestByCustomerId(Long customerId);
}
