package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
}
