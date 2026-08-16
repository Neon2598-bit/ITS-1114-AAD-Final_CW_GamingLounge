package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.StationTypeDTO;

import java.util.List;

public class StationTypeServiceImpl implements StationTypeService{
    @Override
    public void saveStationType(StationTypeDTO stationTypeDTO) {
        
    }

    @Override
    public void updateStationType(StationTypeDTO stationTypeDTO) {

    }

    @Override
    public void deleteStationType(Long id) {

    }

    @Override
    public List<StationTypeDTO> getAllStationTypes() {
        return List.of();
    }

    @Override
    public StationTypeDTO getStationTypeById(Long id) {
        return null;
    }

    @Override
    public List<StationTypeDTO> getAffordableTypes(Double maxRate) {
        return List.of();
    }
}
