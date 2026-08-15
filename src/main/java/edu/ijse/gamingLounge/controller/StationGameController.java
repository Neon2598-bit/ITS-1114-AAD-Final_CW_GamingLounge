package edu.ijse.gamingLounge.controller;


import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.StationGameDTO;
import edu.ijse.gamingLounge.service.StationGameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/station-game")
@RequiredArgsConstructor
@CrossOrigin
public class StationGameController {
    private final StationGameService stationGameService;

    @PostMapping
    public ResponseEntity<CommonResponse> save(@Valid @RequestBody StationGameDTO dto) {
        stationGameService.saveStationGame(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(ResponseCode.CREATED, ResponseMessage.SUCCESS));
    }

    @DeleteMapping("/id")
    public ResponseEntity<CommonResponse> delete(@PathVariable Long id) {
        stationGameService.deleteStationGame(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAll() {
        List<StationGameDTO> list = stationGameService.getAllStationGames();
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    // GET /api/v1/station-game/by-station/1
    @GetMapping("/by-station/{stationId}")
    public ResponseEntity<CommonResponse> getByStation(@PathVariable Long stationId) {
        List<StationGameDTO> list = stationGameService.getGamesByStation(stationId);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    // GET /api/v1/station-game/by-game/1
    @GetMapping("/by-game/{gameId}")
    public ResponseEntity<CommonResponse> getByGame (@PathVariable Long gameId) {
        List<StationGameDTO> list = stationGameService.getStationsByGame(gameId);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }
}
