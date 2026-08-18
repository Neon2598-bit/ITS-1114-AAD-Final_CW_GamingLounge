package edu.ijse.gamingLounge.controller;

import jakarta.validation.Valid;
import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.GameDTO;
import edu.ijse.gamingLounge.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
@CrossOrigin
public class GameController {
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<CommonResponse> save(@Valid @RequestBody GameDTO dto) {
        gameService.saveGame(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(ResponseCode.CREATED, ResponseMessage.SUCCESS));
    }

    @PutMapping
    public ResponseEntity<CommonResponse> update(@Valid @RequestBody GameDTO dto) {
        gameService.updateGame(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> delete(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAll() {
        List<GameDTO> list = gameService.getAllGames();
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getById(@PathVariable Long id) {
        GameDTO dto = gameService.getGameById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, dto, ResponseMessage.SUCCESS));
    }

    // game/search?keyword=fifa
    @GetMapping("/search")
    public ResponseEntity<CommonResponse> search(@RequestParam String keyword) {
        List<GameDTO> list = gameService.searchGamesByName(keyword);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    @GetMapping("/inactive")
    public ResponseEntity<CommonResponse> getInactiveGames() {
        List<GameDTO> list = gameService.getInactiveGames();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<CommonResponse> restoreGame(@PathVariable Long id) {
        gameService.restoreGame(id);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT)
                .body(new CommonResponse(ResponseCode.RESET_CONTENT,"Content Reset Successfully", ResponseMessage.RESET_CONTENT));
    }
}
