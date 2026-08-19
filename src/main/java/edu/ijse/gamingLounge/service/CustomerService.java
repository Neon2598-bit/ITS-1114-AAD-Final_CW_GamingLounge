package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    Long saveCustomer(CustomerDTO dto);
    void updateCustomer(CustomerDTO dto);
    void deleteCustomer(Long id);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(Long id);
    void restoreCustomer(Long id);
    List<CustomerDTO> getInactiveCustomers();
}
