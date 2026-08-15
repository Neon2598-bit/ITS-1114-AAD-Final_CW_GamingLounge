package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.GameDTO;
import edu.ijse.gamingLounge.entity.Game;
import edu.ijse.gamingLounge.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        try {
            Optional<Game> optional = gameRepository.findById(dto.getId());
            if (optional.isPresent()) {
                Game game = optional.get();
                game.setGameName(dto.getGameName());
                game.setGenre(dto.getGenre());
                game.setAgeRating(dto.getAgeRating());
                gameRepository.save(game);
                log.info("Game updated successfully");
            }
        } catch (Exception e) {
            log.error("Couldn't update game from the database", e.getMessage());
        }
    }

    @Override
    public void deleteGame(Long id) {
        try {
            if (gameRepository.existsById(id)) {
                gameRepository.deleteById(id);
                log.info("Game deleted successfully from database");
            }
        } catch (Exception e) {
            log.error("Operation failed: {}", e.getMessage());
        }
    }

    @Override
    public List<GameDTO> getAllGames() {
        List<GameDTO> list = new ArrayList<>();
        try {
            for (Game g : gameRepository.findAll()) {
                list.add(new GameDTO(g.getId(), g.getGameName(), g.getGenre(), g.getAgeRating()));
            }
            log.info("All Games loaded successfully");
        } catch (Exception e) {
            log.error("Couldn't load game list", e.getMessage());
        }
        return list;
    }

    @Override
    public GameDTO getGameById(Long id) {
        try {
            Optional<Game> optional = gameRepository.findById(id);
            if (optional.isPresent()) {
                Game g = optional.get();
                log.info("Specific game has been found");
                return new GameDTO(g.getId(), g.getGameName(), g.getGenre(), g.getAgeRating());
            }
        } catch (Exception e) {
            log.error("Game not found by id", e.getMessage());
        }
        return null;
    }

    @Override
    public List<GameDTO> searchGamesByName(String keyword) {
        List<GameDTO> list = new ArrayList<>();
        try {
            for (Game g : gameRepository.searchByName(keyword)) {
                list.add(new GameDTO(g.getId(), g.getGameName(), g.getGenre(), g.getAgeRating()));
            }
            log.info("Game retrieved successfully from database");
        } catch (Exception e) {
            log.error("Game not found with that name", e.getMessage());
        }
        return list;
    }
}
