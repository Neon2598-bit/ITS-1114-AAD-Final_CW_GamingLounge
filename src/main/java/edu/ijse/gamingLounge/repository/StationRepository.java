package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    @Query("SELECT s FROM Station s JOIN FETCH s.branch JOIN FETCH s.stationType")
    List<Station> findAllWithDetails();
}
