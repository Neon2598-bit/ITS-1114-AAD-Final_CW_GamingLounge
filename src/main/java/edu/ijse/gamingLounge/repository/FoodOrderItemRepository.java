package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.FoodOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderItemRepository extends JpaRepository<FoodOrderItem, Long> {

    @Query("SELECT i FROM FoodOrderItem i JOIN FETCH i.snack WHERE i.foodOrder.id = :orderId")
    List<FoodOrderItem> findByFoodOrder_Id(Long orderId);
}
