package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    //JPQL - searches by partial name, case-insensitive
    @Query("SELECT g FROM Game g WHERE LOWER(g.gameName) LIKE (CONCAT ('%', :keyword, '%'))")
    List<Game> searchByName(String keyword);

    boolean existsByGameName(String gameName);

    List<Game> findByActiveTrue();
    List<Game> findByActiveFalse();

}
