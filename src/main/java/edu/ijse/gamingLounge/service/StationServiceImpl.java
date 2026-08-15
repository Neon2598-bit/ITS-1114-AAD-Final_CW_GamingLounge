package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.repository.BranchRepository;
import edu.ijse.gamingLounge.repository.StationRepository;
import edu.ijse.gamingLounge.repository.StationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationServiceImpl {
    private final StationRepository stationRepository;
    private final BranchRepository branchRepository;
    private final StationTypeRepository stationTypeRepository;
}
