package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.StationGameDTO;

import java.util.List;

public interface StationGameService {
    void saveStationGame(StationGameDTO dto);
    void deleteStationGame(Long id);
    List<StationGameDTO> getAllStationGames();
    List<StationGameDTO> getGamesByStation(Long stationId);
    List<StationGameDTO> getStationsByGame(Long gameId);
    void restoreStationGame(Long id);
    List<StationGameDTO> getInactiveStationGames();
}
