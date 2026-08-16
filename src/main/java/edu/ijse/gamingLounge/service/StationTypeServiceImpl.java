package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.StationTypeDTO;
import edu.ijse.gamingLounge.entity.StationType;
import edu.ijse.gamingLounge.repository.StationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        try {
            if (stationTypeRepository.existsById(id)){
                stationTypeRepository.deleteById(id);
                log.info("Station type has be deleted successfully");
            }
        } catch (Exception e) {
            log.error("Deletion of station type has been failed", e.getMessage());
        }
    }

    @Override
    public List<StationTypeDTO> getAllStationTypes() {
        List<StationTypeDTO> list = new ArrayList<>();
        try {
            List<StationType> stationTypes = stationTypeRepository.findAll();
            for (StationType s : stationTypes) {
                list.add(new StationTypeDTO(s.getId(), s.getTypeName(), s.getHourlyRate()));
            }
            log.info("All station types have been retrieved successfully");
        } catch (Exception e) {
            log.error("Station types couldn't get", e.getMessage());
        }
        return list;
    }

    @Override
    public StationTypeDTO getStationTypeById(Long id) {
        try {
            Optional<StationType> optional = stationTypeRepository.findById(id);

            if (optional.isPresent()) {
                StationType stationType = optional.get();
                log.info("StationType has be retrieved successfully");
                return new StationTypeDTO(stationType.getId(), stationType.getTypeName(), stationType.getHourlyRate());
            }
        } catch (Exception e) {
            log.error("Station type of id { }"+id+"couldn't fetch", e.getMessage());
        }
        return null;
    }

    @Override
    public List<StationTypeDTO> getAffordableTypes(Double maxRate) {
        List<StationTypeDTO> list = new ArrayList<>();

        try {
            List<StationType> stationTypes = stationTypeRepository.findAffordableTypesNative(maxRate);
            for (StationType s : stationTypes) {
                list.add(new StationTypeDTO(s.getId(), s.getTypeName(), s.getHourlyRate()));
            }
            log.info("All station types that can be afford retrieved successfully");
        } catch (Exception e) {
            log.error("No affordable station type found", e.getMessage());
        }
        return list;
    }
}
