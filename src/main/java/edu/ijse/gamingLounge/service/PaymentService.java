package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {
    void savePayment(PaymentDTO dto);
    List<PaymentDTO> getAllPayments();
    PaymentDTO getPaymentById(Long id);
    List<PaymentDTO> getPaymentsByCustomer(Long customerId);
}
