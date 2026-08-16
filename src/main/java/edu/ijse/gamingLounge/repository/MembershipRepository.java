package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Membership;
import edu.ijse.gamingLounge.status.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    boolean existsByCustomer_IdAndStatus(Long customerId, MembershipStatus status);
}
