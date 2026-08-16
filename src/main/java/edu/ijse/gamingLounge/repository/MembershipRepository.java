package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Membership;
import edu.ijse.gamingLounge.status.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    boolean existsByCustomer_IdAndStatus(Long customerId, MembershipStatus status);

    @Query("SELECT m FROM Membership m JOIN FETCH m.customer JOIN FETCH m.membershipPlan")
    List<Membership> findAllWithDetails();

    List<Membership> findByCustomer_Id(Long customerId);
}
