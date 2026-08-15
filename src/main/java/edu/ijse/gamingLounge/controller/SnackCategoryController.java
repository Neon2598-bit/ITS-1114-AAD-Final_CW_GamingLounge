package edu.ijse.gamingLounge.controller;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.SnackCategoryDTO;
import edu.ijse.gamingLounge.service.SnackCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/snack-category")
@RequiredArgsConstructor
@CrossOrigin
public class SnackCategoryController {
    private final SnackCategoryService snackCategoryService;

    @PostMapping
    public ResponseEntity<CommonResponse> save(@Valid @RequestBody SnackCategoryDTO dto) {
        snackCategoryService.saveCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(ResponseCode.CREATED, ResponseMessage.SUCCESS));
    }

    @PutMapping
    public ResponseEntity<CommonResponse> update(@Valid @RequestBody SnackCategoryDTO dto) {
        snackCategoryService.updateCategory(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> delete(@PathVariable Long id) {
        snackCategoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }
}
