package edu.ijse.gamingLounge.controller;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.FoodOrderDTO;
import edu.ijse.gamingLounge.service.FoodOrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/food-order")
@CrossOrigin
public class FoodOrderController {
    private final FoodOrderService foodOrderService;

    @PostMapping
    public ResponseEntity<CommonResponse> save(@Valid @RequestBody FoodOrderDTO dto) {
        foodOrderService.saveFoodOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(ResponseCode.CREATED, ResponseMessage.SUCCESS));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CommonResponse> updateStatus(@PathVariable Long id, @RequestParam String status) {
        foodOrderService.updateOrderStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAll() {
        List<FoodOrderDTO> list = foodOrderService.getAllOrders();
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getById(@PathVariable Long id) {
        FoodOrderDTO dto = foodOrderService.getOrderById(id);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, dto, ResponseMessage.SUCCESS));
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<CommonResponse> getByCustomer(@PathVariable Long customerId) {
        List<FoodOrderDTO> list = foodOrderService.getOrdersByCustomer(customerId);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }
}
