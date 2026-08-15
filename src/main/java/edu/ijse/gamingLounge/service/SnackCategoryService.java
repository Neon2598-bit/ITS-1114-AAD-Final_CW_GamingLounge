package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.SnackCategoryDTO;

import java.util.List;

public interface SnackCategoryService {
    void saveCategory(SnackCategoryDTO dto);
    void updateCategory(SnackCategoryDTO dto);
    void deleteCategory(Long id);
    List<SnackCategoryDTO> getAllCategories();
    SnackCategoryDTO getCategoryById(Long id);
}
