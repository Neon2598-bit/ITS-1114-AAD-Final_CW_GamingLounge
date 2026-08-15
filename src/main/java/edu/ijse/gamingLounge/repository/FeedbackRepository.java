package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
