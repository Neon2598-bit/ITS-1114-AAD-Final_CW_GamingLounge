package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.InvoiceDTO;
import edu.ijse.gamingLounge.entity.Invoice;
import edu.ijse.gamingLounge.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Override
    public List<InvoiceDTO> getAllInvoices() {
        List<InvoiceDTO> list = new ArrayList<>();
        try {
            for (Invoice i : invoiceRepository.findAllWithDetails()) {
                list.add(toDTO(i));
            }
            log.info("Loaded all invoices");
        } catch (Exception e) {
            log.error("Couldn't load invoices", e.getMessage());
        }
        return list;
    }

    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        try {
            Optional<Invoice> optional = invoiceRepository.findById(id);
            if (optional.isPresent()) {
                log.info("Loaded Invoice for id {}", id);
                return toDTO(optional.get());
            }
        } catch (Exception e) {
            log.error("Couldn't load invoice for id {}", id, e.getMessage());
        }
        return null;
    }

    @Override
    public InvoiceDTO getInvoiceByPaymentId(Long paymentId) {
        try {
            Optional<Invoice> optional = invoiceRepository.findByPayment_Id(paymentId);
            if (optional.isPresent()) {
                log.info("Loaded Invoice for id {}", paymentId);
                return toDTO(optional.get());
            }
        } catch (Exception e) {
            log.error("Couldn't load invoice for payment id {} ",paymentId, e.getMessage());
        }
        return null;
    }

    private InvoiceDTO toDTO(Invoice i) {
        return new InvoiceDTO(
                i.getId(),
                i.getInvoiceNumber(),
                i.getIssueDate(),
                i.getPayment().getId(),
                i.getPayment().getAmount(),
                i.getPayment().getCustomer().getName()
        );
    }
}
