package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.MembershipDTO;

import java.util.List;

public interface MembershipService {
    void saveMembership(MembershipDTO dto);
    void updateMembershipStatus(Long id, String status);
    List<MembershipDTO> getAllMemberships();
    MembershipDTO getMembershipById(Long id);
    List<MembershipDTO> getMembershipsByCustomer(Long customerId);
}
