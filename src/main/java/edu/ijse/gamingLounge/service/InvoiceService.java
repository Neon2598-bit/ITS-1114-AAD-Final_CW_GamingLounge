package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.InvoiceDTO;

import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> getAllInvoices();
    InvoiceDTO getInvoiceById(Long id);
    InvoiceDTO getInvoiceByPaymentId(Long paymentId);
}
