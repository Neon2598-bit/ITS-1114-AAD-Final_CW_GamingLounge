package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Payment;
import edu.ijse.gamingLounge.status.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByReferenceIdAndPaymentForAndStatus(Long referenceId, String paymentFor, PaymentStatus status);

    @Query("SELECT p FROM Payment p JOIN FETCH p.customer")
    List<Payment> findAllWithDetails();
}
