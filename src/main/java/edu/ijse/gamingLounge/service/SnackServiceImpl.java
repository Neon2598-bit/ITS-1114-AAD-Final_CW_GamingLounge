package edu.ijse.gamingLounge.service;


import edu.ijse.gamingLounge.dto.SnackDTO;
import edu.ijse.gamingLounge.entity.Snack;
import edu.ijse.gamingLounge.entity.SnackCategory;
import edu.ijse.gamingLounge.repository.SnackCategoryRepository;
import edu.ijse.gamingLounge.repository.SnackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            if (categoryOptional.isPresent()) {
                Snack snack = new Snack();
                snack.setName(dto.getName());
                snack.setPrice(dto.getPrice());
                snack.setStockQty(dto.getStockQty());
                snack.setSnackCategory(categoryOptional.get());
                snackRepository.save(snack);
                log.info("Snack saved successfully to database");
            }
        } catch (Exception e) {
            log.error("Couldn't save snack", e.getMessage());
        }
    }

    @Override
    public void updateSnack(SnackDTO dto) {
        try {
            Optional<Snack> snackOptional = snackRepository.findById(dto.getId());
            Optional<SnackCategory> categoryOptional = snackCategoryRepository.findById(dto.getSnackCategoryId());
            if (snackOptional.isPresent() && categoryOptional.isPresent()) {
                Snack snack = snackOptional.get();
                snack.setName(dto.getName());
                snack.setPrice(dto.getPrice());
                snack.setStockQty(dto.getStockQty());
                snack.setSnackCategory(categoryOptional.get());
                snackRepository.save(snack);
                log.info("Snack updated successfully to database");
            }
        } catch (Exception e) {
            log.error("Snack updation failed", e.getMessage());
        }
    }

    @Override
    public void deleteSnack(Long id) {

    }

    @Override
    public List<SnackDTO> getAllSnacks() {
        return List.of();
    }

    @Override
    public SnackDTO getSnackById(Long id) {
        return null;
    }

    @Override
    public List<SnackDTO> getLowStockSnacks(Integer threshold) {
        return List.of();
    }
}
