package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
    @Query(value = "SELECT * FROM membership_plan ORDER BY price ASC", nativeQuery = true)
    List<MembershipPlan> findAllOrderByPriceNative();

    boolean existsByPlanName(String planName);

    List<MembershipPlan> findByActiveFalse();
}
