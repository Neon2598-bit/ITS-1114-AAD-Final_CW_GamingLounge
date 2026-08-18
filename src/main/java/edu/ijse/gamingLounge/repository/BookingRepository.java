package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b JOIN FETCH b.customer JOIN FETCH b.station")
    List<Booking> findAllWithDetails();

    List<Booking> findByCustomer_Id(Long customerId);
}
