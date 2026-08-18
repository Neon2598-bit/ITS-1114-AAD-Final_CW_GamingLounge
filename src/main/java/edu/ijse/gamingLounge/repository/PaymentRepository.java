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

    List<Payment> findByCustomer_Id(Long customerId);

    @Query(value = "SELECT DATE_FORMAT(payment_date, '%Y-%m') AS month, SUM(amount) AS total " +
            "FROM payment WHERE status = 'COMPLETED' GROUP BY month ORDER BY month", nativeQuery = true)
    List<Object[]> findMonthlyRevenue();
}
