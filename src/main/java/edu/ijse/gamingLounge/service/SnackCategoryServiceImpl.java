package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.SnackCategoryDTO;
import edu.ijse.gamingLounge.entity.SnackCategory;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.SnackCategoryRepository;
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
public class SnackCategoryServiceImpl implements SnackCategoryService {
    private final SnackCategoryRepository snackCategoryRepository;

    @Override
    public void saveCategory(SnackCategoryDTO dto) {
        try {
            SnackCategory category = new SnackCategory();
            if (snackCategoryRepository.existsByCategoryName(dto.getCategoryName())) {
                throw new BusinessException("This category name already exists");
            }
            category.setCategoryName(dto.getCategoryName());
            snackCategoryRepository.save(category);
            log.info("Snack category saved successfully to database");
        } catch (Exception e) {
            log.error("Couldn't save category", e.getMessage());
            throw new BusinessException("Couldn't save category");
        }
    }

    @Override
    public void updateCategory(SnackCategoryDTO dto) {
        Optional<SnackCategory> snackCategoryOptional = snackCategoryRepository.findById(dto.getId());

        if (snackCategoryOptional.isEmpty()) {
            throw new BusinessException("Snack category not found", HttpStatus.NOT_FOUND);
        }

        if (!snackCategoryOptional.get().isActive()) {
            throw new BusinessException("Snack category is not active", HttpStatus.GONE);
        }

        SnackCategory category = snackCategoryOptional.get();
        category.setCategoryName(dto.getCategoryName());
        snackCategoryRepository.save(category);
        log.info("Snack category updated successfully to database");

    }

    @Override
    public void deleteCategory(Long id) {
        try {
            Optional<SnackCategory> optional = snackCategoryRepository.findById(id);
            if (optional.isPresent()) {
                SnackCategory category = optional.get();
                category.setActive(false);
                snackCategoryRepository.save(category);
                log.info("Snack category marked as inactive (soft delete)");
            }
        } catch (Exception e) {
            log.error("Category deletion failed", e.getMessage());
            throw new BusinessException("Category deletion failed, No data found");
        }
    }

    @Override
    public List<SnackCategoryDTO> getAllCategories() {
        List<SnackCategoryDTO> list = new ArrayList<>();
        try {
            for (SnackCategory c : snackCategoryRepository.findByActiveTrue()) {
                list.add(toDTO(c));
            }
            log.info("All categories retrieved successfully");
        } catch (Exception e) {
            log.error("Couldn't retriene categories", e.getMessage());
        }
        return list;
    }

    @Override
    public SnackCategoryDTO getCategoryById(Long id) {
        Optional<SnackCategory> snackCategoryOptional = snackCategoryRepository.findById(id);

        if (snackCategoryOptional.isEmpty()) {
            throw new BusinessException("Snack category not found", HttpStatus.NOT_FOUND);
        }

        SnackCategory snackCategory = snackCategoryOptional.get();

        if (!snackCategory.isActive()) {
            throw new BusinessException("Snack category is not active", HttpStatus.GONE);
        }

        log.info("Snack category retrieved successfully");
        return toDTO(snackCategory);
    }

    @Override
    public void restoreCategory(Long id) {
        Optional<SnackCategory> optional = snackCategoryRepository.findById(id);
        if (optional.isPresent() && !optional.get().isActive()) {
            SnackCategory category = optional.get();
            category.setActive(true);
            snackCategoryRepository.save(category);
            log.info("Snack category restored");
        } else if (optional.isPresent() && optional.get().isActive()) {
            throw new BusinessException("Snack category is already in active state", HttpStatus.BAD_REQUEST);
        } else {
            throw new BusinessException("Snack category not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<SnackCategoryDTO> getInactiveCategories() {
        List<SnackCategoryDTO> list = new ArrayList<>();
        try {
            for (SnackCategory c : snackCategoryRepository.findByActiveFalse()) {
                list.add(new SnackCategoryDTO(c.getId(), c.getCategoryName()));
            }
            log.info("Inactive categories retrieved successfully");
        } catch (Exception e) {
            log.error("Couldn't retrieve inactive categories", e.getMessage());
        }
        return list;
    }

    private SnackCategoryDTO toDTO (SnackCategory snackCategory) {
        return new SnackCategoryDTO(snackCategory.getId(), snackCategory.getCategoryName());
    }
}
