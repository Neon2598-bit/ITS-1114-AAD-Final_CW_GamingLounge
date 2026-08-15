package edu.ijse.gamingLounge.exception;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        log.error("Validation failed: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse(ResponseCode.BAD_REQUEST, errors, "Validation failed"));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse> handleBusinessException(BusinessException ex) {
        log.error("Business rule violation: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatus())
                .body(new CommonResponse(ex.getStatus().value(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleGeneralError(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse(ResponseCode.SERVER_ERROR, "Something went wrong. Please try again."));
    }
}
