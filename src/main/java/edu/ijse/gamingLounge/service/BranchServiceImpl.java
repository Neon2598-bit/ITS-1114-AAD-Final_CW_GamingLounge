package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.BranchDTO;
import edu.ijse.gamingLounge.entity.Branch;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
                throw new BusinessException(
                        "This branch name already exists"
                );
            }
            branch.setBranchName(branchDTO.getBranchName());
            branch.setAddress(branchDTO.getAddress());

            if (branchRepository.existsByContactNumber(branchDTO.getContactNumber())){
                log.error("This number already exists: {}", branchDTO.getContactNumber());
                throw new BusinessException("This number already exists");
            }

            branch.setContactNumber(branchDTO.getContactNumber());
            branchRepository.save(branch);
            log.info("Branch saved successfully to database");
    }

    @Override
    public void updateBranch(BranchDTO branchDTO) {
            Optional<Branch> branchOptional = branchRepository.findById(branchDTO.getId());
            if (branchOptional.isPresent() && branchOptional.get().isActive()) {
                Branch branch = branchOptional.get();

                branch.setBranchName(branchDTO.getBranchName());
                branch.setAddress(branchDTO.getAddress());

                branch.setContactNumber(branchDTO.getContactNumber());
                branchRepository.save(branch);
                log.info("Successfully update branch");

            } else {
                log.error("Couldn't find any branch with this id {}", branchDTO.getId());
                throw new BusinessException("Couldn't find any branch with this id");
            }
    }

    @Override
    public void deleteBranch(Long branchId) {
            Optional<Branch> branchOptional = branchRepository.findById(branchId);

            if (branchOptional.isPresent()) {
                Branch branch = branchOptional.get();
                branch.setActive(false);

                branchRepository.save(branch);

                log.info("Branch marked as inactive");
            } else {
                log.error("Couldn't find any branch with id {}", branchId);
                throw new BusinessException("Couldn't find any branch with id " + branchId);
            }
    }

    @Override
    public List<BranchDTO> getAllBranches() {
        List<BranchDTO> branchDTOList = new ArrayList<>();
        try {
            List<Branch> branchList = branchRepository.findByActiveTrue();
            for (Branch branch : branchList) {
                branchDTOList.add(toDTO(branch));
            }
            log.info("Successfully retrieve all active branches from database");
        } catch (Exception e) {
            log.error("Couldn't load branch list", e.getMessage());
        }
        return branchDTOList;
    }

    @Override
    public BranchDTO getBranchById(Long branchId) {
        Optional<Branch> branchOptional = branchRepository.findById(branchId);

        if (branchOptional.isEmpty()) {
            log.error("Couldn't find any branch with id {}", branchId);
            throw new BusinessException("Couldn't find any branch with id " + branchId, HttpStatus.NOT_FOUND);
        }

        Branch branch = branchOptional.get();

        if (!branch.isActive()) {
            log.info("Branch with id {} is not active", branchId);
            throw new BusinessException("Branch with id " + branchId + " is not active", HttpStatus.NOT_FOUND);
        }

        log.info("Successfully fetch the specific branch from database");
        return toDTO(branch);
    }

    @Override
    public void restoreBranch(Long branchId) {
            Optional<Branch> branchOptional = branchRepository.findById(branchId);
            if (branchOptional.isPresent() && !branchOptional.get().isActive()) {
                Branch branch = branchOptional.get();
                branch.setActive(true);
                branchRepository.save(branch);
                log.info("Branch restored");
            } else if (branchOptional.isPresent() && branchOptional.get().isActive()) {
                throw new BusinessException("Branch with id " + branchId + " already active", HttpStatus.CONFLICT);
            } else if (!branchOptional.isPresent()) {
                throw new BusinessException("Branch with id " + branchId + " not found", HttpStatus.NOT_FOUND);
            }
    }

    @Override
    public List<BranchDTO> getInactiveBranches() {
        List<BranchDTO> branchDTOList = new ArrayList<>();
        try {
            List<Branch> branchList = branchRepository.findByActiveFalse();
            for (Branch branch : branchList) {
                branchDTOList.add(toDTO(branch));
            }
            log.info("Successfully retrieve inactive branches from database");
        } catch (Exception e) {
            log.error("Couldn't load inactive branch list", e.getMessage());
        }
        return branchDTOList;
    }

    private BranchDTO toDTO(Branch branch) {
        return new BranchDTO(
                branch.getId(),
                branch.getBranchName(),
                branch.getAddress(),
                branch.getContactNumber()
        );
    }
}
