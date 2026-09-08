package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.entity.Snack;
import edu.ijse.gamingLounge.util.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockAlertServiceImpl implements StockAlertService {
    private final EmailService emailService;

    @Value("${gaminglounge.admin.email:admin@gaminglounge.com}")
    private String adminEmail;

    @Value("${gaminglounge.lowstock.threshold:5}")
    private int lowStockThreshold;

    @Override
    public void checkAndAlert(Snack snack) {
        if (snack.getStockQty() > lowStockThreshold) {
            return;
        }
        String alertBody = "Low stock alert!\n\n"
                + "Snack   : " + snack.getName() + "\n"
                + "Remaining Qty: " + snack.getStockQty() + "\n"
                + "Threshold: " + lowStockThreshold + "\n\n"
                + "Please restock soon.";
        emailService.sendEmail(adminEmail, "Low Stock Alert - " + snack.getName(), alertBody);
        log.warn("Low stock alert sent for snack {} (qty={})", snack.getName(), snack.getStockQty());
    }
}
