package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.FoodOrderDTO;

import java.util.List;

public interface FoodOrderService {
    void saveFoodOrder(FoodOrderDTO dto);
    void updateOrderStatus(Long id, String status);
    List<FoodOrderDTO> getAllOrders();
    FoodOrderDTO getOrderById(Long id);
    List<FoodOrderDTO> getOrdersByCustomer(Long customerId);
}
