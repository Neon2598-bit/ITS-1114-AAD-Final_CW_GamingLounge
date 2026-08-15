package edu.ijse.gamingLounge.service;


import edu.ijse.gamingLounge.dto.SnackDTO;
import edu.ijse.gamingLounge.entity.Snack;
import edu.ijse.gamingLounge.entity.SnackCategory;
import edu.ijse.gamingLounge.repository.SnackCategoryRepository;
import edu.ijse.gamingLounge.repository.SnackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        try {
            if (snackRepository.existsById(id)) {
                snackRepository.deleteById(id);
                log.info("Specific snack has been deleted");
            }
        } catch (Exception e) {
            log.error("Snack deletion failed", e.getMessage());
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
        try {
            Optional<Snack> optional = snackRepository.findById(id);
            if (optional.isPresent()) {
                log.info("Specific snack retrieved successfully");
                return toDTO(optional.get());
            }
        } catch (Exception e) {
            log.error("Couldn't find this snack", e.getMessage());
        }
        return null;
    }

    @Override
    public List<SnackDTO> getLowStockSnacks(Integer threshold) {
        return List.of();
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
