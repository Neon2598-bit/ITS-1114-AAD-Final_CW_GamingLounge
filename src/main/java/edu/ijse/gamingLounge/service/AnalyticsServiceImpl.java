package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.MonthlyRevenueDTO;
import edu.ijse.gamingLounge.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {
    private final PaymentRepository paymentRepository;

    @Override
    public List<MonthlyRevenueDTO> getMonthlyRevenue() {
        List<MonthlyRevenueDTO> list = new ArrayList<>();
        try {
            for (Object[] row : paymentRepository.findMonthlyRevenue()) {
                String month = (String) row[0];
                Double total = ((Number) row[1]).doubleValue();
                list.add(new MonthlyRevenueDTO(month, total));
            }
            log.info("Monthly Revenue List Loaded");
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return list;
    }
}
