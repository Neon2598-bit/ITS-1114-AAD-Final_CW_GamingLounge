package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.StationGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationGameRepository extends JpaRepository<StationGame, Long> {
    // Prevents adding the SAME game to the SAME station twice
    boolean existsByStation_IdAndGame_Id(Long stationId, Long gameId);
}
