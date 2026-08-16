package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.MembershipDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembershipServiceImpl implements MembershipService {
    @Override
    public void saveMembership(MembershipDTO dto) {

    }

    @Override
    public void updateMembershipStatus(Long id, String status) {

    }

    @Override
    public List<MembershipDTO> getAllMemberships() {
        return List.of();
    }

    @Override
    public MembershipDTO getMembershipById(Long id) {
        return null;
    }

    @Override
    public List<MembershipDTO> getMembershipsByCustomer(Long customerId) {
        return List.of();
    }
}
