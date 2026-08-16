package edu.ijse.gamingLounge.controller;

import edu.ijse.gamingLounge.constant.CommonResponse;
import edu.ijse.gamingLounge.constant.ResponseCode;
import edu.ijse.gamingLounge.constant.ResponseMessage;
import edu.ijse.gamingLounge.dto.MembershipDTO;
import edu.ijse.gamingLounge.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.html.HTMLParagraphElement;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/membership")
public class MembershipController {
    private final MembershipService membershipService;

    @PostMapping
    public ResponseEntity<CommonResponse> save(@Valid @RequestBody MembershipDTO dto) {
        membershipService.saveMembership(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse(ResponseCode.CREATED, ResponseMessage.SUCCESS));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CommonResponse> updateStatus(@PathVariable Long id, @RequestParam String status) {
        membershipService.updateMembershipStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> getAll() {
        List<MembershipDTO> list = membershipService.getAllMemberships();
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getById(@PathVariable Long id) {
        MembershipDTO dto = membershipService.getMembershipById(id);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, dto, ResponseMessage.SUCCESS));
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<CommonResponse> getByCustomer(@PathVariable Long customerId) {
        List<MembershipDTO> list = membershipService.getMembershipsByCustomer(customerId);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(ResponseCode.SUCCESS, list, ResponseMessage.SUCCESS));
    }
}
