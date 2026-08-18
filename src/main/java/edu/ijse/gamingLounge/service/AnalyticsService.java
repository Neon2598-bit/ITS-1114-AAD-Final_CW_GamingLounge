package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.MonthlyRevenueDTO;

import java.util.List;

public interface AnalyticsService {
    List<MonthlyRevenueDTO> getMonthlyRevenue();
}
