package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.MembershipDTO;
import edu.ijse.gamingLounge.entity.Customer;
import edu.ijse.gamingLounge.entity.Membership;
import edu.ijse.gamingLounge.entity.MembershipPlan;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.CustomerRepository;
import edu.ijse.gamingLounge.repository.MembershipPlanRepository;
import edu.ijse.gamingLounge.repository.MembershipRepository;
import edu.ijse.gamingLounge.status.MembershipStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembershipServiceImpl implements MembershipService {
    private final MembershipRepository membershipRepository;
    private final CustomerRepository customerRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    @Override
    public void saveMembership(MembershipDTO dto) {
        Optional<Customer> customerOptional = customerRepository.findById(dto.getCustomerId());
        Optional<MembershipPlan> membershipPlanOptional = membershipPlanRepository.findById(dto.getMembershipPlanId());

        if (customerOptional.isEmpty() || membershipPlanOptional.isEmpty()) {
            log.error("Customer Or Membership Plan Does Not Exist");
            throw new BusinessException("Customer Or Membership Plan Does Not Exist");
        }

        if (membershipRepository.existsByCustomer_IdAndStatus(dto.getCustomerId(), MembershipStatus.ACTIVE)){
            throw new BusinessException("You already have an active membership");
        }

        MembershipPlan membershipPlan = membershipPlanOptional.get();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(membershipPlan.getDurationDays());

        Membership membership = new Membership();

        membership.setCustomer(customerOptional.get());
        membership.setMembershipPlan(membershipPlan);
        membership.setStartDate(startDate);
        membership.setEndDate(endDate);
        membership.setStatus(MembershipStatus.ACTIVE);

        membershipRepository.save(membership);

        log.info("Membership Saved Successfully");

    }

    @Override
    public void updateMembershipStatus(Long id, String status) {
        Optional<Membership>  membershipOptional = membershipRepository.findById(id);

        if (membershipOptional.isEmpty()) {
            log.error("Membership Does Not Exist");
            throw new BusinessException("Membership Does Not Exist");
        }

        Membership membership = membershipOptional.get();

        try {
            membership.setStatus(MembershipStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            log.error("Invalid Membership Status");
            throw new BusinessException("Invalid Membership Status");
        }
        membershipRepository.save(membership);
        log.info("Membership {} status updated to {}",id,status);
    }

    @Override
    public List<MembershipDTO> getAllMemberships() {
        List<MembershipDTO> membershipDTOList = new ArrayList<>();

        try {
            for (Membership membership : membershipRepository.findAllWithDetails()) {
                membershipDTOList.add(toDTO(membership));
            }
        } catch (Exception e) {
            log.error("Membership Load Failed");
        }
        return membershipDTOList;
    }

    @Override
    public MembershipDTO getMembershipById(Long id) {
        return null;
    }

    @Override
    public List<MembershipDTO> getMembershipsByCustomer(Long customerId) {
        return List.of();
    }

    private MembershipDTO toDTO(Membership membership) {
        return new MembershipDTO(
                membership.getId(),
                membership.getCustomer().getId(),
                membership.getMembershipPlan().getId(),
                membership.getStartDate(),
                membership.getEndDate(),
                membership.getStatus().name(),
                membership.getCustomer().getName(),
                membership.getMembershipPlan().getPlanName()
        );
    }
}
