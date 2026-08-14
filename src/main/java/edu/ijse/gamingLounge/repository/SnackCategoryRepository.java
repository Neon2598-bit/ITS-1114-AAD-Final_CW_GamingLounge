package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.SnackCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnackCategoryRepository extends JpaRepository<SnackCategory,Long> {
}
