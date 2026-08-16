package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.StationTypeDTO;
import edu.ijse.gamingLounge.entity.StationType;
import edu.ijse.gamingLounge.repository.StationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationTypeServiceImpl implements StationTypeService{
    private final StationTypeRepository stationTypeRepository;

    @Override
    public void saveStationType(StationTypeDTO stationTypeDTO) {
        try {
            StationType stationType = new StationType();
            stationType.setTypeName(stationTypeDTO.getTypeName());
            stationType.setHourlyRate(stationTypeDTO.getHourlyRate());
            stationTypeRepository.save(stationType);
            log.info("StationType save successful");

        } catch (Exception ex) {
            log.error("StationType save failed");
        }
    }

    @Override
    public void updateStationType(StationTypeDTO stationTypeDTO) {
        try {
            Optional<StationType> optional = stationTypeRepository.findById(stationTypeDTO.getId());
            if (optional.isPresent()) {
                StationType stationType = optional.get();
                stationType.setTypeName(stationTypeDTO.getTypeName());
                stationType.setHourlyRate(stationTypeDTO.getHourlyRate());
                stationTypeRepository.save(stationType);
                log.info("Station type has be updated successfully");
            }
        } catch (Exception e) {
            log.error("Updation of station type has been failed", e.getMessage());
        }
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
