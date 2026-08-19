package edu.ijse.gamingLounge.controller;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.StationDTO;
import edu.ijse.gamingLounge.service.StationService;
import edu.ijse.gamingLounge.status.StationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/station")
@RequiredArgsConstructor
@CrossOrigin
public class StationController {
    private final StationService stationService;

    @PostMapping
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

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> delete(@PathVariable Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAll() {
        List<StationDTO> list = stationService.getAllStations();
        if (list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, list, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getById(@PathVariable Long id) {
        StationDTO dto = stationService.getStationById(id);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, dto, ResponseMessage.SUCCESS));
    }

    @GetMapping("/available")
    public ResponseEntity<CommonResponse> getAvailable(@RequestParam Long branchId) {
        List<StationDTO> list = stationService.getAvailableStationsByBranch(branchId);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    @GetMapping("/inactive")
    public ResponseEntity<CommonResponse> getInactiveStations() {
        List<StationDTO> list = stationService.getInactiveStations();
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, list, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<CommonResponse> restoreStation(@PathVariable Long id) {
        stationService.restoreStation(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }
}
