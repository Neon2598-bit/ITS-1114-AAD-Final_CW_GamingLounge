package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Snack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnackRepository extends JpaRepository<Snack, Long> {
    @Query("SELECT s FROM Snack s JOIN FETCH s.snackCategory")
    List<Snack> findAllWithCategory();
}
