package edu.ijse.gamingLounge.service;

import java.util.List;

public interface StationService {
    void saveStation(StationDTO dto);
    void updateStation(StationDTO dto);
    void deleteStation(Long id);
    List<StationDTO> getAllStations();
    StationDTO getStationById(Long id);
    List<StationDTO> getAvailableStationsByBranch(Long branchId);
}
