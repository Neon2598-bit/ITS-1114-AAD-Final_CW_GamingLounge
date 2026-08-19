package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.SnackCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnackCategoryRepository extends JpaRepository<SnackCategory,Long> {
    boolean existsByCategoryName(String categoryName);
    List<SnackCategory> findByActiveTrue();
    List<SnackCategory> findByActiveFalse();
}
