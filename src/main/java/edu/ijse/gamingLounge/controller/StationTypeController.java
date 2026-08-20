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

    @PutMapping
    public ResponseEntity<CommonResponse> update(@Valid @RequestBody StationTypeDTO dto) {
        stationTypeService.updateStationType(dto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> delete (@PathVariable Long id) {
        stationTypeService.deleteStationType(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAll () {
        List<StationTypeDTO> list = stationTypeService.getAllStationTypes();

        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getById (@PathVariable Long id) {
        StationTypeDTO dto = stationTypeService.getStationTypeById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, dto, ResponseMessage.SUCCESS));
    }

    // /station-type/affordable?maxRate=300
    @GetMapping("/affordable")
    public ResponseEntity<CommonResponse> getAffordable (@RequestParam Double maxRate) {
        List<StationTypeDTO> stationTypeDTOList = stationTypeService.getAffordableTypes(maxRate);

        if (stationTypeDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, stationTypeDTOList, ResponseMessage.NOT_FOUND));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, stationTypeDTOList, ResponseMessage.SUCCESS));
    }

    @GetMapping("/inactive")
    public ResponseEntity<CommonResponse> getInactive() {
        List<StationTypeDTO> list = stationTypeService.getInactiveStationTypes();
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, list, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<CommonResponse> restore(@PathVariable Long id) {
        stationTypeService.restoreStationType(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }
}
