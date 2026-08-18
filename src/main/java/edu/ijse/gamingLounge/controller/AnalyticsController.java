package edu.ijse.gamingLounge.controller;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.MonthlyRevenueDTO;
import edu.ijse.gamingLounge.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@CrossOrigin
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/monthly-revenue")
    public ResponseEntity<CommonResponse> getMonthlyRevenue() {
        List<MonthlyRevenueDTO> list = analyticsService.getMonthlyRevenue();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }
}
