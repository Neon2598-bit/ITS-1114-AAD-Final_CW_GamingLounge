package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.GameDTO;

import java.util.List;

public interface GameService {
    void saveGame(GameDTO gameDTO);
    void updateGame(GameDTO gameDTO);
    void deleteGame(Long id);
    List<GameDTO> getAllGames();
    GameDTO getGameById(Long id);
    List<GameDTO> searchGamesByName(String keyword);
    void restoreGame(Long id);
    List<GameDTO> getInactiveGames();
}
