package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.StationTypeDTO;
import edu.ijse.gamingLounge.entity.StationType;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.StationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
            StationType stationType = new StationType();
            if (stationTypeRepository.existsByTypeName(stationTypeDTO.getTypeName())) {
                throw new BusinessException("This type name is already in use");
            }
            stationType.setTypeName(stationTypeDTO.getTypeName());
            stationType.setHourlyRate(stationTypeDTO.getHourlyRate());
            stationTypeRepository.save(stationType);
            log.info("StationType save successful");

    }

    @Override
    public void updateStationType(StationTypeDTO stationTypeDTO) {
            Optional<StationType> optional = stationTypeRepository.findById(stationTypeDTO.getId());

            if (optional.isEmpty()) {
                throw new BusinessException("This type id is not exist");
            }

            if (!optional.get().isActive()) {
                throw new BusinessException("This type id is not active");
            }

            StationType stationType = optional.get();
            stationType.setTypeName(stationTypeDTO.getTypeName());
            stationType.setHourlyRate(stationTypeDTO.getHourlyRate());
            stationTypeRepository.save(stationType);
            log.info("Station type has be updated successfully");
    }

    @Override
    public void deleteStationType(Long id) {
        try {
            Optional<StationType> optional = stationTypeRepository.findById(id);
            if (optional.isPresent()) {
                StationType stationType = optional.get();
                stationType.setActive(false);
                stationTypeRepository.save(stationType);
                log.info("Station type marked as inactive (soft delete)");
            }
        } catch (Exception e) {
            log.error("Deletion failed", e.getMessage());
            throw new BusinessException("Deletion failed", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<StationTypeDTO> getAllStationTypes() {
        List<StationTypeDTO> list = new ArrayList<>();
        try {
            List<StationType> stationTypes = stationTypeRepository.findByActiveTrue();
            for (StationType s : stationTypes) {
                list.add(toDTO(s));
            }
            log.info("All station types have been retrieved successfully");
        } catch (Exception e) {
            log.error("Station types couldn't get", e.getMessage());
        }
        return list;
    }

    @Override
    public StationTypeDTO getStationTypeById(Long id) {
            Optional<StationType> stationTypeOptional = stationTypeRepository.findById(id);

            if (stationTypeOptional.isEmpty()) {
                throw new BusinessException("This type id is not exist", HttpStatus.NOT_FOUND);
            }

            StationType stationType = stationTypeOptional.get();

            if (!stationType.isActive()) {
                throw new BusinessException("This type id is not active", HttpStatus.GONE);
            }

            log.info("Station type has be retrieved successfully");
            return toDTO(stationType);
    }

    @Override
    public List<StationTypeDTO> getAffordableTypes(Double maxRate) {
        List<StationTypeDTO> list = new ArrayList<>();
        try {
            List<StationType> stationTypes = stationTypeRepository.findAffordableTypesNative(maxRate);
            for (StationType s : stationTypes) {
                list.add(toDTO(s));
            }
            log.info("All station types that can be afford retrieved successfully");
        } catch (Exception e) {
            log.error("No affordable station type found", e.getMessage());
        }
        return list;
    }

    @Override
    public void restoreStationType(Long id) {
        Optional<StationType> stationTypeOptional = stationTypeRepository.findById(id);

        if (stationTypeOptional.isPresent() && !stationTypeOptional.get().isActive()) {
            StationType stationType = stationTypeOptional.get();
            stationType.setActive(true);
            stationTypeRepository.save(stationType);
            log.info("Station type restored");
        } else if (stationTypeOptional.isPresent()) {
            throw new BusinessException("This station type already active", HttpStatus.BAD_REQUEST);
        } else {
            throw new BusinessException("No station type found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<StationTypeDTO> getInactiveStationTypes() {
        List<StationTypeDTO> list = new ArrayList<>();
        try {
            for (StationType s : stationTypeRepository.findByActiveFalse()) {
                list.add(toDTO(s));
            }
            log.info("Inactive station types retrieved successfully");
        } catch (Exception e) {
            log.error("Couldn't retrieve inactive station types", e.getMessage());
        }
        return list;
    }

    private StationTypeDTO toDTO(StationType stationType) {
        return new StationTypeDTO(stationType.getId(), stationType.getTypeName(), stationType.getHourlyRate());
    }
}
