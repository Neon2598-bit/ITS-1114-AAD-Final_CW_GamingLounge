package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.StationTypeDTO;

import java.util.List;

public interface StationTypeService {
    void saveStationType(StationTypeDTO stationTypeDTO);
    void updateStationType(StationTypeDTO stationTypeDTO);
    void deleteStationType(Long id);
    List<StationTypeDTO> getAllStationTypes();
    StationTypeDTO getStationTypeById(Long id);
    List<StationTypeDTO> getAffordableTypes(Double maxRate);
    void restoreStationType(Long id);
    List<StationTypeDTO> getInactiveStationTypes();
}
