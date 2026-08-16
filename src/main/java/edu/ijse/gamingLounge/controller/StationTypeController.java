package edu.ijse.gamingLounge.controller;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.StationTypeDTO;
import edu.ijse.gamingLounge.service.StationTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/station-type")
@RequiredArgsConstructor
@CrossOrigin
public class StationTypeController {
    private final StationTypeService stationTypeService;

    @PostMapping
    public ResponseEntity<CommonResponse> save(@Valid @RequestBody StationTypeDTO dto) {
        stationTypeService.saveStationType(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(ResponseCode.CREATED, ResponseMessage.SUCCESS));
    }
}
