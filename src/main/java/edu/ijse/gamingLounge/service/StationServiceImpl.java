package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.StationDTO;
import edu.ijse.gamingLounge.entity.Branch;
import edu.ijse.gamingLounge.entity.Station;
import edu.ijse.gamingLounge.entity.StationType;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.BranchRepository;
import edu.ijse.gamingLounge.repository.StationRepository;
import edu.ijse.gamingLounge.repository.StationTypeRepository;
import edu.ijse.gamingLounge.status.StationStatus;
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
public class StationServiceImpl implements StationService {
    private final StationRepository stationRepository;
    private final BranchRepository branchRepository;
    private final StationTypeRepository stationTypeRepository;

    @Override
    public void saveStation(StationDTO dto) {
        Optional<Branch> branchOptional = branchRepository.findById(dto.getBranchId());
        Optional<StationType> typeOptional = stationTypeRepository.findById(dto.getStationTypeId());

        if (branchOptional.isEmpty() || typeOptional.isEmpty()) {
            throw new BusinessException("Branch or type not found.", HttpStatus.NOT_FOUND);
        }

        if (!branchOptional.get().isActive() || !typeOptional.get().isActive()) {
            throw new BusinessException("Branch or type is not in active state.",HttpStatus.GONE);
        }

        Station station = new Station();
        station.setStationCode(dto.getStationCode());
        station.setStatus(StationStatus.valueOf(dto.getStatus()));
        station.setBranch(branchOptional.get());
        station.setStationType(typeOptional.get());
        stationRepository.save(station);
        log.info("Station saved successfully to database");
    }

    @Override
    public void updateStation(StationDTO dto) {
        Optional<Station> stationOptional = stationRepository.findById(dto.getId());
        Optional<Branch> branchOptional = branchRepository.findById(dto.getBranchId());
        Optional<StationType> typeOptional = stationTypeRepository.findById(dto.getStationTypeId());

        if (stationOptional.isEmpty() || branchOptional.isEmpty() || typeOptional.isEmpty()) {
            throw new BusinessException("Branch,Station Type or Station not found.", HttpStatus.NOT_FOUND);
        }

        if (!branchOptional.get().isActive() || !typeOptional.get().isActive() || !stationOptional.get().isActive()) {
            throw new BusinessException("Branch,Station Type or Station Is Not In Active State.", HttpStatus.GONE);
        }
        Station station = stationOptional.get();
        station.setStationCode(dto.getStationCode());
        station.setStatus(StationStatus.valueOf(dto.getStatus()));
        station.setBranch(branchOptional.get());
        station.setStationType(typeOptional.get());
        stationRepository.save(station);
        log.info("Station updated successfully ...!");

    }

    @Override
    public void deleteStation(Long id) {
        Optional<Station> stationOptional = stationRepository.findById(id);
        if  (stationOptional.isEmpty()) {
            throw new BusinessException("Station not found.", HttpStatus.NOT_FOUND);
        }
        Station station = stationOptional.get();
        station.setActive(false);
        stationRepository.save(station);
        log.info("Station marked as inactive (soft delete)");
    }

    @Override
    public List<StationDTO> getAllStations() {
        List<StationDTO> list = new ArrayList<>();
        try {
            for (Station s : stationRepository.findAllWithDetails()) {
                list.add(toDTO(s));
            }
            log.info("All stations retrieved successfully");
        } catch (Exception e) {
            log.error("Couldn't fetch all stations", e.getMessage());
        }
        return list;
    }

    @Override
    public StationDTO getStationById(Long id) {
        try {
            Optional<Station> optional = stationRepository.findById(id);
            if (optional.isPresent()) {
                log.info("Station retrieved successfully");
                return toDTO(optional.get());
            }
        } catch (Exception e) {
            log.error("Can't find specific station", e.getMessage());
        }
        return null;
    }

    @Override
    public List<StationDTO> getAvailableStationsByBranch(Long branchId) {
        List<StationDTO> list = new ArrayList<>();
        try {
            List<Station> stations = stationRepository.findByBranch_IdAndStatusAndActiveTrue(branchId, StationStatus.AVAILABLE);
            for (Station s : stations) {
                list.add(toDTO(s));
            }
            log.info("Currently available stations retrieved successfully according to the specific branch");
        } catch (Exception e) {
            log.error("No available stations at specific branch", e.getMessage());
        }
        return list;
    }

    @Override
    public void restoreStation(Long id) {
        Optional<Station> stationOptional = stationRepository.findById(id);
        if (stationOptional.isEmpty()) {
            throw new BusinessException("Station not found.", HttpStatus.NOT_FOUND);
        }
        if (stationOptional.get().isActive()) {
            throw new BusinessException("Station is already active.", HttpStatus.BAD_REQUEST);
        }
        Station station = stationOptional.get();
        station.setActive(true);
        stationRepository.save(station);
        log.info("Station restored");
    }

    @Override
    public List<StationDTO> getInactiveStations() {
        List<StationDTO> list = new ArrayList<>();
        try {
            for (Station s : stationRepository.findByActiveFalse()) {
                list.add(toDTO(s));
            }
            log.info("Inactive stations retrieved successfully");
        } catch (Exception e) {
            log.error("Couldn't retrieve inactive stations", e.getMessage());
        }
        return list;
    }

    private StationDTO toDTO(Station s) {
        return new StationDTO(
                s.getId(),
                s.getStationCode(),
                s.getStatus().name(),
                s.getBranch().getId(),
                s.getStationType().getId(),
                s.getBranch().getBranchName(),
                s.getStationType().getTypeName(),
                s.getStationType().getHourlyRate()
        );
    }
}
