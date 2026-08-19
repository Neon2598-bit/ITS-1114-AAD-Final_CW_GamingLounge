package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.StationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StationTypeRepository extends JpaRepository<StationType, Long> {

    @Query(value = "SELECT * FROM station_type WHERE hourly_rate <= :maxRate AND active = 1 ORDER BY hourly_rate ASC", nativeQuery = true)
    List<StationType> findAffordableTypesNative(Double maxRate);

    boolean existsByTypeName(String typeName);

    List<StationType> findByActiveTrue();

    List<StationType> findByActiveFalse();
}
