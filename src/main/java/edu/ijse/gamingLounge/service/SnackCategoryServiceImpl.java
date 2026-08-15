package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.SnackCategoryDTO;
import edu.ijse.gamingLounge.entity.SnackCategory;
import edu.ijse.gamingLounge.repository.SnackCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnackCategoryServiceImpl implements SnackCategoryService {
    private final SnackCategoryRepository snackCategoryRepository;

    @Override
    public void saveCategory(SnackCategoryDTO dto) {
        try {
            SnackCategory category = new SnackCategory();
            category.setCategoryName(dto.getCategoryName());
            snackCategoryRepository.save(category);
            log.info("Snack category saved successfully to database");
        } catch (Exception e) {
            log.error("Couldn't save category", e.getMessage());
        }
    }

    @Override
    public void updateCategory(SnackCategoryDTO dto) {
        try {
            Optional<SnackCategory> optional = snackCategoryRepository.findById(dto.getId());
            if (optional.isPresent()) {
                SnackCategory category = optional.get();
                category.setCategoryName(dto.getCategoryName());
                snackCategoryRepository.save(category);
                log.info("Snack category updated successfully to database");
            }
        } catch (Exception e) {
            log.error("Snack Category updation failed", e.getMessage());
        }
    }

    @Override
    public void deleteCategory(Long id) {

    }

    @Override
    public List<SnackCategoryDTO> getAllCategories() {
        return List.of();
    }

    @Override
    public SnackCategoryDTO getCategoryById(Long id) {
        return null;
    }
}
