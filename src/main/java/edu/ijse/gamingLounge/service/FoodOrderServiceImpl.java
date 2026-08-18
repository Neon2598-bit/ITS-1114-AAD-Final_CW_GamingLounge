package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.FoodOrderDTO;
import edu.ijse.gamingLounge.dto.FoodOrderItemDTO;
import edu.ijse.gamingLounge.entity.*;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.*;
import edu.ijse.gamingLounge.status.MembershipStatus;
import edu.ijse.gamingLounge.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class FoodOrderServiceImpl implements FoodOrderService{
    private final FoodOrderRepository foodOrderRepository;
    private final FoodOrderItemRepository foodOrderItemRepository;
    private final CustomerRepository customerRepository;
    private final SnackRepository snackRepository;
    private final MembershipRepository membershipRepository;

    @Override
    @Transactional
    public void saveFoodOrder(FoodOrderDTO dto) {
        Optional<Customer> customerOptional = customerRepository.findById(dto.getCustomerId());
        if (customerOptional.isEmpty()) {
            log.error("Customer not found: {}", dto.getCustomerId());
            throw new BusinessException("Customer not found.");
        }

        List<Snack> snacksInOrder = new ArrayList<>();
        for (FoodOrderItemDTO item : dto.getItems()) {
            Optional<Snack> snackOptional = snackRepository.findById(item.getSnackId());
            if (snackOptional.isEmpty()) {
                log.error("Snack not found: {}", item.getSnackId());
                throw new BusinessException("One of the selected snacks no longer exists.");
            }
            Snack snack = snackOptional.get();
            if (snack.getStockQty() < item.getQuantity()) {
                log.error("Not enough stock for {}: requested {}, available {}",
                        snack.getName(), item.getQuantity(), snack.getStockQty());
                throw new BusinessException(
                        "Not enough stock for " + snack.getName() + " (only " + snack.getStockQty() + " left).");
            }
            snacksInOrder.add(snack);
        }

        FoodOrder foodOrder = new FoodOrder();
        foodOrder.setCustomer(customerOptional.get());
        foodOrder.setOrderDate(LocalDateTime.now());
        foodOrder.setStatus(OrderStatus.PENDING);

        double totalAmount = 0.0;
        for (int i = 0; i < dto.getItems().size(); i++) {
            FoodOrderItemDTO itemDTO = dto.getItems().get(i);
            Snack snack = snacksInOrder.get(i);
            totalAmount += snack.getPrice() * itemDTO.getQuantity();
        }

        // Same membership discount used for bookings, applied here too
        Optional<Membership> activeMembership =
                membershipRepository.findFirstByCustomer_IdAndStatus(
                        dto.getCustomerId(), MembershipStatus.ACTIVE);
        if (activeMembership.isPresent()) {
            double discountPercentage = activeMembership.get().getMembershipPlan().getDiscountPercentage();
            totalAmount = totalAmount * (1 - discountPercentage / 100.0);
            log.info("Applied {}% membership discount for customer {}", discountPercentage, dto.getCustomerId());
        }
        foodOrder.setTotalAmount(totalAmount);
        foodOrderRepository.save(foodOrder);
        log.info("Food order saved successfully for customer {}", customerOptional.get().getName());

        for (int i = 0; i < dto.getItems().size(); i++) {
            FoodOrderItemDTO itemDTO = dto.getItems().get(i);
            Snack snack = snacksInOrder.get(i);

            FoodOrderItem item = new FoodOrderItem();
            item.setFoodOrder(foodOrder);
            item.setSnack(snack);
            item.setQuantity(itemDTO.getQuantity());
            item.setSubTotal(snack.getPrice() * itemDTO.getQuantity());
            foodOrderItemRepository.save(item);

            // Reduce stock now that the order is confirmed
            snack.setStockQty(snack.getStockQty() - itemDTO.getQuantity());
            snackRepository.save(snack);
        }
        log.info("Saved successfully to database");
    }

    @Override
    public void updateOrderStatus(Long id, String status) {
        Optional<FoodOrder> optional = foodOrderRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BusinessException("Order not found.");
        }
        FoodOrder foodOrder = optional.get();
        try {
            foodOrder.setStatus(OrderStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid order status: " + status);
        }
        foodOrderRepository.save(foodOrder);
        log.info("Order {} status updated to {}", id, status);
    }

    @Override
    public List<FoodOrderDTO> getAllOrders() {
        List<FoodOrderDTO> list = new ArrayList<>();
        try {
            for (FoodOrder o : foodOrderRepository.findAllWithDetails()) {
                list.add(toDTO(o));
            }
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public FoodOrderDTO getOrderById(Long id) {
        try {
            Optional<FoodOrder> optional = foodOrderRepository.findById(id);
            if (optional.isPresent()) {
                return toDTO(optional.get());
            }
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<FoodOrderDTO> getOrdersByCustomer(Long customerId) {
        List<FoodOrderDTO> list = new ArrayList<>();
        try {
            for (FoodOrder o : foodOrderRepository.findByCustomer_Id(customerId)) {
                list.add(toDTO(o));
            }
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return list;
    }

    private FoodOrderDTO toDTO(FoodOrder o) {
        List<FoodOrderItemDTO> items = new ArrayList<>();
        for (FoodOrderItem item : foodOrderItemRepository.findByFoodOrder_Id(o.getId())) {
            items.add(new FoodOrderItemDTO(
                    item.getId(),
                    item.getSnack().getId(),
                    item.getQuantity(),
                    item.getSubTotal(),
                    item.getSnack().getName()
            ));
        }
        return new FoodOrderDTO(
                o.getId(),
                o.getOrderDate(),
                o.getStatus().name(),
                o.getTotalAmount(),
                o.getCustomer().getId(),
                o.getCustomer().getName(),
                items
        );
    }
}
