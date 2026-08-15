package edu.ijse.gamingLounge.repository;

import edu.ijse.gamingLounge.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    boolean existsByBranchName(String branchName);
    boolean existsByContactNumber(String contactNumber);
}
