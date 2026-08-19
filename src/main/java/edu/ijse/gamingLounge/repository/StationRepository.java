package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Station;
import edu.ijse.gamingLounge.status.StationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    @Query("SELECT s FROM Station s JOIN FETCH s.branch JOIN FETCH s.stationType WHERE s.active = true")
    List<Station> findAllWithDetails();

    List<Station> findByBranch_IdAndStatusAndActiveTrue(Long branchId, StationStatus status);
    List<Station> findByActiveFalse();
}
