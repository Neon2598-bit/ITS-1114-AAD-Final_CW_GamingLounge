package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.MembershipPlanDTO;
import edu.ijse.gamingLounge.entity.MembershipPlan;
import edu.ijse.gamingLounge.exception.BusinessException;
import edu.ijse.gamingLounge.repository.MembershipPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembershipPlanServiceImpl implements MembershipPlanService {
    private final MembershipPlanRepository membershipPlanRepository;

    @Override
    public void savePlan(MembershipPlanDTO dto) {
            MembershipPlan plan = new MembershipPlan();

            if (membershipPlanRepository.existsByPlanName(dto.getPlanName())) {
                log.info("Membership Plan Name already exists in DB");
                throw new BusinessException("Membership Plan Name already exists in DB");
            }

            plan.setPlanName(dto.getPlanName());

            plan.setPrice(dto.getPrice());
            plan.setDurationDays(dto.getDurationDays());
            plan.setDiscountPercentage(dto.getDiscountPercentage());
            membershipPlanRepository.save(plan);
            log.info("Membership plan saved successfully to database.");
    }

    @Override
    public void updatePlan(MembershipPlanDTO dto) {
            Optional<MembershipPlan> optional = membershipPlanRepository.findById(dto.getId());
            if (optional.isPresent() && optional.get().isActive()) {
                MembershipPlan plan = optional.get();

                plan.setPlanName(dto.getPlanName());
                plan.setPrice(dto.getPrice());
                plan.setDurationDays(dto.getDurationDays());
                plan.setDiscountPercentage(dto.getDiscountPercentage());
                membershipPlanRepository.save(plan);
                log.info("Update membership plan successfully to database.");
            } else if (optional.isEmpty()) {
                log.info("Membership Plan not found in DB");
                throw new BusinessException("Membership Plan not found in DB");
            }
    }

    @Override
    public void deletePlan(Long id) {
            Optional<MembershipPlan> optional = membershipPlanRepository.findById(id);
            if (optional.isPresent()) {
                MembershipPlan plan = optional.get();
                plan.setActive(false);
                membershipPlanRepository.save(plan);
                log.info("Membership plan marked as inactive");
            } else {
                log.info("Membership Plan not found in DB for that ID");
                throw new BusinessException("Membership Plan not found in DB for that ID");
            }
    }

    @Override
    public List<MembershipPlanDTO> getAllPlans() {
        List<MembershipPlanDTO> list = new ArrayList<>();
        try {
            for (MembershipPlan p : membershipPlanRepository.findAllOrderByPriceNative()) {
                list.add(toDTO(p));
            }
            log.info("Membership plans found successfully from database.");
        } catch (Exception e) {
            log.error("Couldn't fetch all membership plans", e.getMessage());
        }
        return list;
    }

    @Override
    public MembershipPlanDTO getPlanById(Long id) {
        Optional<MembershipPlan> membershipPlanOptional = membershipPlanRepository.findById(id);

        if (membershipPlanOptional.isEmpty()) {
            throw new BusinessException("Membership Plan not found in DB for that ID");
        }

        MembershipPlan membershipPlan = membershipPlanOptional.get();

        if (!membershipPlan.isActive()) {
            throw new BusinessException("This Membership Plan is not active");
        }

        log.info("Specific membership plan retrieve successfully from database.");
        return toDTO(membershipPlan);
    }

    @Override
    public void restorePlan(Long id) {
            Optional<MembershipPlan> optional = membershipPlanRepository.findById(id);
            if (optional.isPresent() && !optional.get().isActive()) {
                MembershipPlan plan = optional.get();
                plan.setActive(true);
                membershipPlanRepository.save(plan);
                log.info("Membership plan restored");
            } else if (optional.isPresent() && optional.get().isActive()) {
                throw new BusinessException("Membership Plan With ID " + id + "Already Active ", HttpStatus.CONFLICT);
            } else if (optional.isEmpty()) {
                log.info("Membership Plan not found in DB for that ID");
                throw new BusinessException("Membership Plan not found in DB for that ID");
            }
    }

    @Override
    public List<MembershipPlanDTO> getInactivePlans() {
        List<MembershipPlanDTO> list = new ArrayList<>();
        try {
            for (MembershipPlan p : membershipPlanRepository.findByActiveFalse()) {
                list.add(new MembershipPlanDTO(p.getId(), p.getPlanName(), p.getPrice(), p.getDurationDays(), p.getDiscountPercentage()));
            }
            log.info("Inactive membership plans found successfully from database.");
        } catch (Exception e) {
            log.error("Couldn't fetch inactive membership plans", e.getMessage());
        }
        return list;
    }

    private MembershipPlanDTO toDTO(MembershipPlan plan) {
        return new MembershipPlanDTO(
                plan.getId(), plan.getPlanName(), plan.getPrice(), plan.getDurationDays(), plan.getDiscountPercentage()
        );
    }
}
