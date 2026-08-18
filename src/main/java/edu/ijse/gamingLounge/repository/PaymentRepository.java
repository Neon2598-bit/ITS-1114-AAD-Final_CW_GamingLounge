package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Payment;
import edu.ijse.gamingLounge.status.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByReferenceIdAndPaymentForAndStatus(Long referenceId, String paymentFor, PaymentStatus status);
}
