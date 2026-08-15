package edu.ijse.gamingLounge.service;


import edu.ijse.gamingLounge.dto.SnackDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnackServiceImpl implements SnackService {

    @Override
    public void saveSnack(SnackDTO dto) {

    }

    @Override
    public void updateSnack(SnackDTO dto) {

    }

    @Override
    public void deleteSnack(Long id) {

    }

    @Override
    public List<SnackDTO> getAllSnacks() {
        return List.of();
    }

    @Override
    public SnackDTO getSnackById(Long id) {
        return null;
    }

    @Override
    public List<SnackDTO> getLowStockSnacks(Integer threshold) {
        return List.of();
    }
}
