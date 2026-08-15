package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.BranchDTO;
import edu.ijse.gamingLounge.entity.Branch;
import edu.ijse.gamingLounge.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;

    @Override
    public void saveBranch(BranchDTO branchDTO) {
        try {
            Branch branch = new Branch();
            branch.setBranchName(branchDTO.getBranchName());
            branch.setAddress(branchDTO.getAddress());
            branch.setContactNumber(branchDTO.getContactNumber());
            branchRepository.save(branch);
            log.info("Branch saved successfully to database");
        } catch (Exception e) {
            log.error("Couldn't save branch to the database", e.getMessage());
        }
    }

    @Override
    public void updateBranch(BranchDTO branchDTO) {
        try {
            Optional<Branch> branchOptional = branchRepository.findById(branchDTO.getId());
            if (branchOptional.isPresent()) {
                Branch branch = branchOptional.get();
                branch.setBranchName(branchDTO.getBranchName());
                branch.setAddress(branchDTO.getAddress());
                branch.setContactNumber(branchDTO.getContactNumber());
                branchRepository.save(branch);
                log.info("Successfully update branch");
            }
        } catch (Exception e) {
            log.error("Couldn't update the branch", e.getMessage());
        }
    }

    @Override
    public void deleteBranch(Long branchId) {
        try {
            Optional<Branch> branchOptional = branchRepository.findById(branchId);
            if (branchOptional.isPresent()) {
                branchRepository.deleteById(branchId);
                log.info("Branch deleted successfully from database");
            } else {
                log.error("Couldn't find any branch with id {}", branchId);
            }
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
    }

    @Override
    public List<BranchDTO> getAllBranches() {
        List<BranchDTO> branchDTOList = new ArrayList<>();
        try {
            List<Branch> branchList = branchRepository.findAll();
            for (Branch branch : branchList) {
                branchDTOList.add(new BranchDTO(
                        branch.getId(),
                        branch.getBranchName(),
                        branch.getAddress(),
                        branch.getContactNumber()
                ));
            }
            log.info("Successfully retrieve all branches from database");
        } catch (Exception e) {
            log.error("Couldn't load branch list", e.getMessage());
        }
        return branchDTOList;
    }

    @Override
    public BranchDTO getBranchById(Long branchId) {
        try {
            Optional<Branch> branchOptional = branchRepository.findById(branchId);
            if (branchOptional.isPresent()) {
                Branch branch = branchOptional.get();
                log.info("Successfully fetch the specific branch from database");
                return new BranchDTO(
                        branch.getId(),
                        branch.getBranchName(),
                        branch.getAddress(),
                        branch.getContactNumber()
                );
            }

        } catch (Exception e) {
            log.error("Couldn't find the branch", e.getMessage());
        }
        return null;
    }
}
