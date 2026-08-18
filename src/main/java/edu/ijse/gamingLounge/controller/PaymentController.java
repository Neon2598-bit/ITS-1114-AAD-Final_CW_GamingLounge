package edu.ijse.gamingLounge.controller;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.PaymentDTO;
import edu.ijse.gamingLounge.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<CommonResponse> save (@Valid @RequestBody PaymentDTO paymentDTO) {
        paymentService.savePayment(paymentDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.CREATED, ResponseMessage.SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAll() {
        List<PaymentDTO> paymentDTOList = paymentService.getAllPayments();

        if (paymentDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND,ResponseMessage.NOT_FOUND));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, paymentDTOList, ResponseMessage.SUCCESS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getById (@PathVariable Long id) {
        PaymentDTO paymentDTO = paymentService.getPaymentById(id);

        if (paymentDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND,ResponseMessage.NOT_FOUND));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, paymentDTO, ResponseMessage.SUCCESS));
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<CommonResponse> getByCustomer (@PathVariable Long customerId) {
        List<PaymentDTO> paymentDTOList = paymentService.getPaymentsByCustomer(customerId);

        if (paymentDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND,ResponseMessage.NOT_FOUND));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, paymentDTOList, ResponseMessage.SUCCESS));
    }
}
