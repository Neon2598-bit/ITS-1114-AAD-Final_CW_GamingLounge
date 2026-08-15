package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.StationDTO;
import edu.ijse.gamingLounge.entity.Branch;
import edu.ijse.gamingLounge.entity.Station;
import edu.ijse.gamingLounge.entity.StationType;
import edu.ijse.gamingLounge.repository.BranchRepository;
import edu.ijse.gamingLounge.repository.StationRepository;
import edu.ijse.gamingLounge.repository.StationTypeRepository;
import edu.ijse.gamingLounge.status.StationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        try {
            Optional<Branch> branchOptional = branchRepository.findById(dto.getBranchId());
            Optional<StationType> typeOptional = stationTypeRepository.findById(dto.getStationTypeId());

            if (branchOptional.isPresent() && typeOptional.isPresent()) {
                Station station = new Station();
                station.setStationCode(dto.getStationCode());
                station.setStatus(StationStatus.valueOf(dto.getStatus()));
                station.setBranch(branchOptional.get());
                station.setStationType(typeOptional.get());
                stationRepository.save(station);
                log.info("Station saved successfully to database");
            }
        } catch (Exception e) {
            log.error("Couldn't done saving...!", e.getMessage());
        }
    }

    @Override
    public void updateStation(StationDTO dto) {
        try {
            Optional<Station> stationOptional = stationRepository.findById(dto.getId());
            Optional<Branch> branchOptional = branchRepository.findById(dto.getBranchId());
            Optional<StationType> typeOptional = stationTypeRepository.findById(dto.getStationTypeId());

            if (stationOptional.isPresent() && branchOptional.isPresent() && typeOptional.isPresent()) {
                Station station = stationOptional.get();
                station.setStationCode(dto.getStationCode());
                station.setStatus(StationStatus.valueOf(dto.getStatus()));
                station.setBranch(branchOptional.get());
                station.setStationType(typeOptional.get());
                stationRepository.save(station);
                log.info("Station updated successfully ...!");
            }
        } catch (Exception e) {
            log.error("Updation failed....", e.getMessage());
        }
    }

    @Override
    public void deleteStation(Long id) {
        try {
            if (stationRepository.existsById(id)) {
                stationRepository.deleteById(id);
                log.info("Specific station has been deleted successfully");
            }
        } catch (Exception e) {
            log.error("Operation failed: Couldn't delete", e.getMessage());
        }
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
        return List.of();
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
