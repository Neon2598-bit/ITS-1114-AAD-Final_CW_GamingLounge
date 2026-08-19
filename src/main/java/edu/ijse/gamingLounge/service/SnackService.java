package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.SnackDTO;

import java.util.List;

public interface SnackService {
    void saveSnack(SnackDTO dto);
    void updateSnack(SnackDTO dto);
    void deleteSnack(Long id);
    List<SnackDTO> getAllSnacks();
    SnackDTO getSnackById(Long id);
    List<SnackDTO> getLowStockSnacks(Integer threshold);
    void restoreSnack(Long id);
    List<SnackDTO> getInactiveSnacks();
}
