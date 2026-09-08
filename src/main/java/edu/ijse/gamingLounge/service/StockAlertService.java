package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.entity.Snack;

public interface StockAlertService {
    void checkAndAlert(Snack snack);
}
