package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.BranchDTO;

import java.util.List;

public interface BranchService {
    void saveBranch(BranchDTO branchDTO);
    void updateBranch(BranchDTO branchDTO);
    void deleteBranch(Long branchId);
    List<BranchDTO> getAllBranches();
    BranchDTO getBranchById(Long branchId);

    void restoreBranch(Long branchId);
    List<BranchDTO> getInactiveBranches();
}
