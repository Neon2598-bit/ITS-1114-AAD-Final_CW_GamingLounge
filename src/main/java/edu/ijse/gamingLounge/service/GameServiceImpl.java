package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.GameDTO;
import edu.ijse.gamingLounge.entity.Game;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.GameRepository;
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
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public void saveGame(GameDTO dto) {
        try {
            Game game = new Game();

            if (gameRepository.existsByGameName(dto.getGameName())) {
                log.info("Game with name {} already exists", dto.getGameName());
                throw new BusinessException("Game with name " + dto.getGameName() + " already exists", HttpStatus.CONFLICT);
            }

            game.setGameName(dto.getGameName());
            game.setGenre(dto.getGenre());
            game.setAgeRating(dto.getAgeRating());
            gameRepository.save(game);
            log.info("Game saved successfully to database");
        } catch (Exception e) {
            log.error("Couldn't save game to the database", e.getMessage());
        }
    }

    @Override
    public void updateGame(GameDTO dto) {
        Optional<Game> optional = gameRepository.findById(dto.getId());

        if (optional.isEmpty()) {
            log.info("Game with id {} not found", dto.getId());
            throw new BusinessException("Game with id " + dto.getId() + " not found", HttpStatus.NOT_FOUND);
        }

        if (!optional.get().isActive()) {
            throw new BusinessException("Game with id " + dto.getId() + " is not active", HttpStatus.GONE);
        }

        Game game = optional.get();

        if (gameRepository.existsByGameName(dto.getGameName())) {
            log.info("Game with name {} already exists", dto.getGameName());
            throw new BusinessException("Game with name " + dto.getGameName() + " already exists", HttpStatus.CONFLICT);
        }

        game.setGameName(dto.getGameName());
        game.setGenre(dto.getGenre());
        game.setAgeRating(dto.getAgeRating());
        gameRepository.save(game);
        log.info("Game updated successfully");
    }

    @Override
    public void deleteGame(Long id) {
        try {
            Optional<Game> optional = gameRepository.findById(id);
            if (optional.isPresent()) {
                Game game = optional.get();
                game.setActive(false);
                gameRepository.save(game);
                log.info("Game marked as inactive");
            }
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
            throw new BusinessException("No Data Found For This ID :", HttpStatus.NO_CONTENT);
        }
    }

    @Override
    public List<GameDTO> getAllGames() {
        List<GameDTO> list = new ArrayList<>();
        try {
            for (Game g : gameRepository.findByActiveTrue()) {
                list.add(toDTO(g));
            }
            log.info("All Games loaded successfully");
        } catch (Exception e) {
            log.error("Couldn't load game list", e.getMessage());
        }
        return list;
    }

    @Override
    public GameDTO getGameById(Long id) {
        Optional<Game> gameOptional = gameRepository.findById(id);

        if (gameOptional.isEmpty()) {
            throw new BusinessException("Game with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        Game game =  gameOptional.get();

        if (!game.isActive()) {
            throw new BusinessException("Game with id " + id + " is not active", HttpStatus.GONE);
        }

        log.info("Game loaded successfully");
        return toDTO(game);

    }

    @Override
    public List<GameDTO> searchGamesByName(String keyword) {
        List<GameDTO> list = new ArrayList<>();
        try {
            for (Game g : gameRepository.searchByName(keyword)) {
                list.add(toDTO(g));
            }
            log.info("Game retrieved successfully from database");
        } catch (Exception e) {
            log.error("Game not found with that name", e.getMessage());
        }
        return list;
    }

    @Override
    public void restoreGame(Long id) {
            Optional<Game> optional = gameRepository.findById(id);
            if (optional.isPresent() && !optional.get().isActive()) {
                Game game = optional.get();
                game.setActive(true);
                gameRepository.save(game);
                log.info("Game restored");
            } else if (optional.isPresent() && optional.get().isActive()) {
                throw new BusinessException("Game with id " + id + " is already active", HttpStatus.CONFLICT);
            } else if (!optional.isPresent()) {
                throw new BusinessException("Game with id " + id + " not found", HttpStatus.NOT_FOUND);
            }
    }

    @Override
    public List<GameDTO> getInactiveGames() {
        List<GameDTO> list = new ArrayList<>();
        try {
            for (Game g : gameRepository.findByActiveFalse()) {
                list.add(toDTO(g));
            }
            log.info("Inactive games loaded successfully");
        } catch (Exception e) {
            log.error("Couldn't load inactive game list", e.getMessage());
        }
        return list;
    }

    private GameDTO toDTO(Game game) {
        return new GameDTO(game.getId(), game.getGameName(), game.getGenre(), game.getAgeRating());
    }
}
