package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.StationGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationGameRepository extends JpaRepository<StationGame, Long> {

    @Query("SELECT sg FROM StationGame sg JOIN FETCH sg.station JOIN FETCH sg.game WHERE sg.active = true")
    List<StationGame> findAllWithDetails();

    List<StationGame> findByStation_IdAndActiveTrue(Long stationId);

    List<StationGame> findByGame_IdAndActiveTrue(Long gameId);

    boolean existsByStation_IdAndGame_IdAndActiveTrue(Long stationId, Long gameId);

    List<StationGame> findByActiveFalse();
}
