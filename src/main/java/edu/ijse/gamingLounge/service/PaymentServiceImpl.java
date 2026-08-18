package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.PaymentDTO;
import edu.ijse.gamingLounge.entity.Customer;
import edu.ijse.gamingLounge.entity.Invoice;
import edu.ijse.gamingLounge.entity.Payment;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.*;
import edu.ijse.gamingLounge.status.BookingStatus;
import edu.ijse.gamingLounge.status.PaymentMethod;
import edu.ijse.gamingLounge.status.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository  bookingRepository;
    private final FoodOrderRepository  foodOrderRepository;
    private final MembershipRepository membershipRepository;

    @Override
    @Transactional
    public void savePayment(PaymentDTO dto) {
        Optional<Customer> customerOptional = customerRepository.findById(dto.getCustomerId());

        if (customerOptional.isEmpty()) {
            log.error("Customer not found", dto.getCustomerId());
            throw new BusinessException("Customer not found");
        }

        PaymentMethod paymentMethod;

        try {
            paymentMethod = PaymentMethod.valueOf(dto.getPaymentMethod());
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment method", dto.getPaymentMethod());
            throw new BusinessException("Invalid payment method");
        }

        double expectedAmount;

        switch (dto.getPaymentFor()) {
            case "BOOKING" -> {
                var booking = bookingRepository.findById(dto.getReferenceId())
                        .orElseThrow(() -> new BusinessException("Booking not found"));

                if (!booking.getCustomer().getId().equals(dto.getCustomerId())) {
                    throw new BusinessException("This booking does not belong to you.");
                }

                if (booking.getStatus() == BookingStatus.CANCELLED) {
                    throw new BusinessException("Cannot pay for a cancelled booking.");
                }

                expectedAmount = booking.getTotalAmount();
            }

            case "FOOD_ORDER" -> {
                var order =  foodOrderRepository.findById(dto.getReferenceId())
                        .orElseThrow(() -> new BusinessException("Food order not found"));

                if (!order.getCustomer().getId().equals(dto.getCustomerId())) {
                    throw new BusinessException("This order does not belong to you.");
                }
                expectedAmount = order.getTotalAmount();
            }

            case "MEMBERSHIP" -> {
                var membership = membershipRepository.findById(dto.getReferenceId())
                        .orElseThrow(() -> new BusinessException("Membership not found"));

                if (!membership.getCustomer().getId().equals(dto.getCustomerId())) {
                    throw new BusinessException("This membership does not belong to you.");
                }

                expectedAmount = membership.getMembershipPlan().getPrice();
            }

            default -> throw new BusinessException("Unknown payment purpose: " + dto.getPaymentFor());
        }

        if (Math.abs(dto.getAmount() - expectedAmount) > 0.01) {
            throw new BusinessException( "Amount does not match what's owed (expected Rs. " + expectedAmount + ").");
        }

        if (paymentRepository.existsByReferenceIdAndPaymentForAndStatus(
                dto.getReferenceId(), dto.getPaymentFor(), PaymentStatus.COMPLETED
        )) {
            throw new BusinessException("This has already been paid for.");
        }

        Payment payment = new Payment();

        payment.setCustomer(customerOptional.get());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentFor(dto.getPaymentFor());
        payment.setReferenceId(dto.getReferenceId());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        log.info("Payment saved successfully for customer {}", customerOptional.get().getName());

        Invoice invoice = new Invoice();
        invoice.setPayment(payment);
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setInvoiceNumber("INV - " + payment.getId() + " - " +
                LocalDateTime.now().toLocalDate().toString().replace(" - ", " "));

        invoiceRepository.save(invoice);

        log.info("Invoice {} generated for payment {}", invoice.getInvoiceNumber(), payment.getId());
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<PaymentDTO> paymentDTOList = new ArrayList<>();

        try {
            for (Payment payment : paymentRepository.findAllWithDetails()) {
                paymentDTOList.add(toDTO(payment));
            }
            log.info("All payments found successfully for payment {}", paymentDTOList);
        } catch (Exception e) {
            log.error("Error while fetching all payments for payment {}", paymentDTOList);
        }
        return paymentDTOList;
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        try {
            Optional<Payment> paymentOptional = paymentRepository.findById(id);

            if (paymentOptional.isPresent()) {
                log.info("Payment found successfully for payment {}", id);
                return toDTO(paymentOptional.get());
            }
        } catch (Exception e) {
            log.error("Error while fetching payment for payment {}", id);
        }
        return null;
    }

    @Override
    public List<PaymentDTO> getPaymentsByCustomer(Long customerId) {
        List<PaymentDTO> paymentDTOList = new ArrayList<>();

        try {
            for (Payment payment : paymentRepository.findByCustomer_Id(customerId)) {
                paymentDTOList.add(toDTO(payment));
            }
            log.info("All payments found successfully for customer {} ", customerId);
        } catch (Exception e) {
            log.error("Error while fetching payments for customer {}", customerId);
        }
        return paymentDTOList;
    }

    private PaymentDTO toDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod().name(),
                payment.getPaymentFor(),
                payment.getReferenceId(),
                payment.getCustomer().getId(),
                payment.getStatus().name(),
                payment.getPaymentDate(),
                payment.getCustomer().getName()
        );
    }
}
