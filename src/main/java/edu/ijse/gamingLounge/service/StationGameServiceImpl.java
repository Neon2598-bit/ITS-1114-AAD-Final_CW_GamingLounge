package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.StationGameDTO;
import edu.ijse.gamingLounge.entity.Game;
import edu.ijse.gamingLounge.entity.Station;
import edu.ijse.gamingLounge.entity.StationGame;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.GameRepository;
import edu.ijse.gamingLounge.repository.StationGameRepository;
import edu.ijse.gamingLounge.repository.StationRepository;
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
public class StationGameServiceImpl implements StationGameService {
    private final StationGameRepository stationGameRepository;
    private final StationRepository stationRepository;
    private final GameRepository gameRepository;

    @Override
    public void saveStationGame(StationGameDTO dto) {
            if (stationGameRepository.existsByStation_IdAndGame_IdAndActiveTrue(dto.getStationId(), dto.getGameId())) {
                log.error("This game is already linked to this station");
                throw new BusinessException(
                        "This game is already linked to this station.");
            }

            Optional<Station> stationOptional = stationRepository.findById(dto.getStationId());
            Optional<Game> gameOptional = gameRepository.findById(dto.getGameId());

            if (stationOptional.isEmpty() || gameOptional.isEmpty()) {
                log.error("Station or Game not found");
                throw new BusinessException("Station or game not found.", HttpStatus.NOT_FOUND);
            }

            if (!stationOptional.get().isActive() || !gameOptional.get().isActive()) {
                throw new BusinessException("Station or Game not in active state.");
            }

            StationGame stationGame = new StationGame();
            stationGame.setStation(stationOptional.get());
            stationGame.setGame(gameOptional.get());
            stationGameRepository.save(stationGame);
            log.info("This station game saved successfully to database");
    }

    @Override
    public void deleteStationGame(Long id) {
            Optional<StationGame> optional = stationGameRepository.findById(id);
            if (optional.isPresent() && optional.get().isActive()) {
                StationGame stationGame = optional.get();
                stationGame.setActive(false);
                stationGameRepository.save(stationGame);
                log.info("Station game link marked as inactive");
            } else {
                log.error("Station game not found");
                throw new BusinessException("Station game not found", HttpStatus.NOT_FOUND);
            }
    }

    @Override
    public List<StationGameDTO> getAllStationGames() {
        List<StationGameDTO> list = new ArrayList<>();
        try {
            for (StationGame sg : stationGameRepository.findAllWithDetails()) {
                list.add(toDTO(sg));
            }
            log.info("All stationGames retrieved successfully");
        } catch (Exception e) {
            log.error("Couldn't retrieve station games", e.getMessage());
        }
        return list;
    }

    @Override
    public List<StationGameDTO> getGamesByStation(Long stationId) {
        List<StationGameDTO> list = new ArrayList<>();
        try {
            for (StationGame sg : stationGameRepository.findByStation_IdAndActiveTrue(stationId)) {
                list.add(toDTO(sg));
            }
            log.info("All Games retrieved according to the specific station");
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public List<StationGameDTO> getStationsByGame(Long gameId) {
        List<StationGameDTO> list = new ArrayList<>();
        try {
            for (StationGame sg : stationGameRepository.findByGame_IdAndActiveTrue(gameId)) {
                list.add(toDTO(sg));
            }
            log.info("All stations retrieved according to the specific game");
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
        return list;
    }

    @Override
    public void restoreStationGame(Long id) {
        Optional<StationGame> stationGameOptional = stationGameRepository.findById(id);

        if (stationGameOptional.isEmpty()) {
            throw new BusinessException("Station game not found", HttpStatus.NOT_FOUND);
        }

        if (stationGameOptional.get().isActive()) {
            throw new BusinessException("Station game is already active.", HttpStatus.CONFLICT);
        }
        StationGame stationGame = stationGameOptional.get();
        stationGame.setActive(true);
        stationGameRepository.save(stationGame);
        log.info("Station game link restored");

    }

    @Override
    public List<StationGameDTO> getInactiveStationGames() {
        List<StationGameDTO> list = new ArrayList<>();
        try {
            for (StationGame sg : stationGameRepository.findByActiveFalse()) {
                list.add(toDTO(sg));
            }
            log.info("Inactive station games retrieved successfully");
        } catch (Exception e) {
            log.error("Couldn't retrieve inactive station games", e.getMessage());
        }
        return list;
    }

    private StationGameDTO toDTO(StationGame sg) {
        return new StationGameDTO(
                sg.getId(),
                sg.getStation().getId(),
                sg.getGame().getId(),
                sg.getStation().getStationCode(),
                sg.getGame().getGameName()
        );
    }
}
