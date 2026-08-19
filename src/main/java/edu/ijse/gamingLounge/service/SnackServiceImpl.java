package edu.ijse.gamingLounge.service;


import edu.ijse.gamingLounge.dto.SnackDTO;
import edu.ijse.gamingLounge.entity.Snack;
import edu.ijse.gamingLounge.entity.SnackCategory;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.SnackCategoryRepository;
import edu.ijse.gamingLounge.repository.SnackRepository;
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
public class SnackServiceImpl implements SnackService {
    private final SnackRepository snackRepository;
    private final SnackCategoryRepository snackCategoryRepository;

    @Override
    public void saveSnack(SnackDTO dto) {
        try {
            Optional<SnackCategory> categoryOptional = snackCategoryRepository.findById(dto.getSnackCategoryId());
            if (categoryOptional.isPresent() && categoryOptional.get().isActive()) {
                Snack snack = new Snack();
                if (snackRepository.existsByName(dto.getName())) {
                    throw new BusinessException("Snack name already exists");
                }
                snack.setName(dto.getName());
                snack.setPrice(dto.getPrice());
                snack.setStockQty(dto.getStockQty());
                snack.setSnackCategory(categoryOptional.get());
                snackRepository.save(snack);
                log.info("Snack saved successfully to database");
            }
        } catch (Exception e) {
            log.error("Couldn't save snack", e.getMessage());
            throw new BusinessException("Couldn't save snack");
        }
    }

    @Override
    public void updateSnack(SnackDTO dto) {
        Optional<Snack> snackOptional = snackRepository.findById(dto.getId());
        Optional<SnackCategory> categoryOptional = snackCategoryRepository.findById(dto.getSnackCategoryId());
        if (snackOptional.isEmpty() || categoryOptional.isEmpty()) {
            throw new BusinessException("Can't find any snack or category with those id's",HttpStatus.NOT_FOUND);
        }
        if (!snackOptional.get().isActive() || !categoryOptional.get().isActive()) {
            throw new BusinessException("Snack or category is not active", HttpStatus.GONE);
        }
        Snack snack = snackOptional.get();
        snack.setName(dto.getName());
        snack.setPrice(dto.getPrice());
        snack.setStockQty(dto.getStockQty());
        snack.setSnackCategory(categoryOptional.get());
        snackRepository.save(snack);
        log.info("Snack updated successfully to database");
    }

    @Override
    public void deleteSnack(Long id) {
        try {
            Optional<Snack> optional = snackRepository.findById(id);
            if (optional.isPresent()) {
                Snack snack = optional.get();
                snack.setActive(false);
                snackRepository.save(snack);
                log.info("Snack marked as inactive (soft delete)");
            }
        } catch (Exception e) {
            log.error("Snack deletion failed", e.getMessage());
            throw new BusinessException("Snack deletion failed");
        }
    }

    @Override
    public List<SnackDTO> getAllSnacks() {
        List<SnackDTO> list = new ArrayList<>();
        try {
            for (Snack s : snackRepository.findAllWithCategory()) {
                list.add(toDTO(s));
            }
            log.info("All snacks retrieved successfully");
        } catch (Exception e) {
            log.error("Couldn't retrieve snack list", e.getMessage());
        }
        return list;
    }

    @Override
    public SnackDTO getSnackById(Long id) {
        Optional<Snack> snackOptional = snackRepository.findById(id);

        if (snackOptional.isEmpty()) {
            throw new BusinessException("Snack not found", HttpStatus.NOT_FOUND);
        }

        Snack snack = snackOptional.get();

        if (!snack.isActive()) {
            throw new BusinessException("Snack is not active", HttpStatus.GONE);
        }
        log.info("Snack retrieved successfully");
        return toDTO(snack);
    }

    @Override
    public List<SnackDTO> getLowStockSnacks(Integer threshold) {
        List<SnackDTO> list = new ArrayList<>();
        try {
            for (Snack s : snackRepository.findLowStockNative(threshold)) {
                list.add(toDTO(s));
            }
            log.info("All snacks that low stock retrieved successfully");
        } catch (Exception e) {
            log.error("Couldn't find low stock snacks", e.getMessage());
        }
        return list;
    }

    @Override
    public void restoreSnack(Long id) {
        Optional<Snack> snackOptional = snackRepository.findById(id);

        if (snackOptional.isPresent() && !snackOptional.get().isActive()) {
            Snack snack = snackOptional.get();
            snack.setActive(true);
            snackRepository.save(snack);
            log.info("Snack restored");
        } else if (snackOptional.isPresent() && snackOptional.get().isActive()) {
            throw new BusinessException("Snack already active", HttpStatus.BAD_REQUEST);
        } else {
            throw new BusinessException("Snack not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<SnackDTO> getInactiveSnacks() {
        List<SnackDTO> list = new ArrayList<>();
        try {
            for (Snack s : snackRepository.findByActiveFalse()) {
                list.add(toDTO(s));
            }
            log.info("Inactive snacks retrieved successfully");
        } catch (Exception e) {
            log.error("Couldn't retrieve inactive snacks", e.getMessage());
        }
        return list;
    }

    private SnackDTO toDTO(Snack s) {
        return new SnackDTO(
                s.getId(),
                s.getName(),
                s.getPrice(),
                s.getStockQty(),
                s.getSnackCategory().getId(),
                s.getSnackCategory().getCategoryName()
        );
    }
}
