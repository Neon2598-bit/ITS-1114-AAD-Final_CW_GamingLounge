package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.StationGameDTO;
import edu.ijse.gamingLounge.entity.Game;
import edu.ijse.gamingLounge.entity.Station;
import edu.ijse.gamingLounge.entity.StationGame;
import edu.ijse.gamingLounge.repository.GameRepository;
import edu.ijse.gamingLounge.repository.StationGameRepository;
import edu.ijse.gamingLounge.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationGameServiceImpl implements StationGameService {
    private final StationGameRepository stationGameRepository;
    private final StationRepository stationRepository;
    private final GameRepository gameRepository;

    @Override
    public void saveStationGame(StationGameDTO dto) {
        try {
            if (stationGameRepository.existsByStation_IdAndGame_Id(dto.getStationId(), dto.getGameId())) {
                log.error("This game is already linked to this station");
                return;
            }

            Optional<Station> stationOptional = stationRepository.findById(dto.getStationId());
            Optional<Game> gameOptional = gameRepository.findById(dto.getGameId());

            if (stationOptional.isPresent() && gameOptional.isPresent()) {
                StationGame stationGame = new StationGame();
                stationGame.setStation(stationOptional.get());
                stationGame.setGame(gameOptional.get());
                stationGameRepository.save(stationGame);
                log.info("This station game saved successfully to database");
            }
        } catch (Exception e) {
            log.error("Saving has been failed", e.getMessage());
        }
    }

    @Override
    public void deleteStationGame(Long id) {
        try {
            if (stationGameRepository.existsById(id)) {
                stationGameRepository.deleteById(id);
                log.info("Specific station game deleted successfully");
            }
        } catch (Exception e) {
            log.error("Couldn't delete specific station game", e.getMessage());
        }
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
