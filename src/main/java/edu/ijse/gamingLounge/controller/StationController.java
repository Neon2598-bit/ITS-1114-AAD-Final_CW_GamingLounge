package edu.ijse.gamingLounge.controller;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.StationDTO;
import edu.ijse.gamingLounge.service.StationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/station")
@RequiredArgsConstructor
@CrossOrigin
public class StationController {
    private final StationService stationService;

    public ResponseEntity<CommonResponse> save(@Valid @RequestBody StationDTO dto){
        stationService.saveStation(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(ResponseCode.CREATED, ResponseMessage.SUCCESS));
    }

    @PutMapping
    public ResponseEntity<CommonResponse> update(@Valid @RequestBody StationDTO dto) {
        stationService.updateStation(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }
}
