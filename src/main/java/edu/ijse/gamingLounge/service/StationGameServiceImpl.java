package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.StationGameDTO;
import edu.ijse.gamingLounge.repository.GameRepository;
import edu.ijse.gamingLounge.repository.StationGameRepository;
import edu.ijse.gamingLounge.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationGameServiceImpl implements StationGameService {
    private final StationGameRepository stationGameRepository;
    private final StationRepository stationRepository;
    private final GameRepository gameRepository;

    @Override
    public void saveStationGame(StationGameDTO dto) {
        
    }

    @Override
    public void deleteStationGame(Long id) {

    }

    @Override
    public List<StationGameDTO> getAllStationGames() {
        return List.of();
    }

    @Override
    public List<StationGameDTO> getGamesByStation(Long stationId) {
        return List.of();
    }

    @Override
    public List<StationGameDTO> getStationsByGame(Long gameId) {
        return List.of();
    }
}
