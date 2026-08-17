package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i JOIN FETCH i.payment p JOIN FETCH p.customer")
    List<Invoice> findAllWithDetails();

    Optional<Invoice> findByPayment_Id(Long paymentId);

}
