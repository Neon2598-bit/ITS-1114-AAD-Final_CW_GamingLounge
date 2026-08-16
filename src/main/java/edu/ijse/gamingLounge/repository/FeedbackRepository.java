package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByBooking_Id(Long bookingId);

    @Query("SELECT f FROM Feedback f JOIN FETCH f.customer JOIN FETCH f.booking")
    List<Feedback> findAllWithDetails();
}
