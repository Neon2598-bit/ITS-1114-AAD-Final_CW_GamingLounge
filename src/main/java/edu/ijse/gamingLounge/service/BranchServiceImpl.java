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
            Branch branch = new Branch();
            if (branchRepository.existsByBranchName(branchDTO.getBranchName())){
                log.error("This branch name already exists: {}", branchDTO.getBranchName());
                throw new edu.ijse.gamingLounge.exception.BusinessException(
                        "This branch name already exists"
                );
            }
            branch.setBranchName(branchDTO.getBranchName());
            branch.setAddress(branchDTO.getAddress());

            if (branchRepository.existsByContactNumber(branchDTO.getContactNumber())){
                log.error("This number already exists: {}", branchDTO.getContactNumber());
                throw new edu.ijse.gamingLounge.exception.BusinessException("This number already exists");
            }

            branch.setContactNumber(branchDTO.getContactNumber());
            branchRepository.save(branch);
            log.info("Branch saved successfully to database");
    }

    @Override
    public void updateBranch(BranchDTO branchDTO) {
            Optional<Branch> branchOptional = branchRepository.findById(branchDTO.getId());
            if (branchOptional.isPresent()) {
                Branch branch = branchOptional.get();

                if (branchRepository.existsByBranchName(branchDTO.getBranchName())){
                    log.error("This branch name already exists: {}", branchDTO.getBranchName());
                    throw new edu.ijse.gamingLounge.exception.BusinessException(
                            "This branch name already exists"
                    );
                }

                branch.setBranchName(branchDTO.getBranchName());
                branch.setAddress(branchDTO.getAddress());

                if (branchRepository.existsByContactNumber(branchDTO.getContactNumber())){
                    log.error("This number already exists: {}", branchDTO.getContactNumber());
                    throw new edu.ijse.gamingLounge.exception.BusinessException("This number already exists");
                }

                branch.setContactNumber(branchDTO.getContactNumber());
                branchRepository.save(branch);
                log.info("Successfully update branch");

            } else {
                log.error("Couldn't find any branch with this id {}", branchDTO.getId());
                throw new edu.ijse.gamingLounge.exception.BusinessException("Couldn't find any branch with this id");
            }
    }

    @Override
    public void deleteBranch(Long branchId) {
            Optional<Branch> branchOptional = branchRepository.findById(branchId);
            if (branchOptional.isPresent()) {
                branchRepository.deleteById(branchId);
                log.info("Branch deleted successfully from database");
            } else {
                log.error("Couldn't find any branch with id {}", branchId);
                throw new edu.ijse.gamingLounge.exception
                        .BusinessException("Couldn't find any branch with id " + branchId);
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
