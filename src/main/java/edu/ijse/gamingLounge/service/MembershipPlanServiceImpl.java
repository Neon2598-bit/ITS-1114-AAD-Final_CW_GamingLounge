package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.MembershipPlanDTO;
import edu.ijse.gamingLounge.entity.MembershipPlan;
import edu.ijse.gamingLounge.repository.MembershipPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        try {
            MembershipPlan plan = new MembershipPlan();
            plan.setPlanName(dto.getPlanName());
            plan.setPrice(dto.getPrice());
            plan.setDurationDays(dto.getDurationDays());
            plan.setDiscountPercentage(dto.getDiscountPercentage());
            membershipPlanRepository.save(plan);
            log.info("Membership plan saved successfully to database.");
        } catch (Exception e) {
            log.error("Couldn't save membership plan", e.getMessage());
        }
    }

    @Override
    public void updatePlan(MembershipPlanDTO dto) {
        try {
            Optional<MembershipPlan> optional = membershipPlanRepository.findById(dto.getId());
            if (optional.isPresent()) {
                MembershipPlan plan = optional.get();
                plan.setPlanName(dto.getPlanName());
                plan.setPrice(dto.getPrice());
                plan.setDurationDays(dto.getDurationDays());
                plan.setDiscountPercentage(dto.getDiscountPercentage());
                membershipPlanRepository.save(plan);
                log.info("Update membership plan successfully to database.");
            }
        } catch (Exception e) {
            log.error("Couldn't update the specific membership plan", e.getMessage());
        }
    }

    @Override
    public void deletePlan(Long id) {
        try {
            if (membershipPlanRepository.existsById(id)) {
                membershipPlanRepository.deleteById(id);
                log.info("Membership plan deleted successfully from database");
            }
        } catch (Exception e) {
            log.error("Deletion failed", e.getMessage());
        }
    }

    @Override
    public List<MembershipPlanDTO> getAllPlans() {
        List<MembershipPlanDTO> list = new ArrayList<>();
        try {
            for (MembershipPlan p : membershipPlanRepository.findAllOrderByPriceNative()) {
                list.add(new MembershipPlanDTO(p.getId(), p.getPlanName(), p.getPrice(), p.getDurationDays(), p.getDiscountPercentage()));
            }
            log.info("Membership plans found successfully from database.");
        } catch (Exception e) {
            log.error("Couldn't fetch all membership plans", e.getMessage());
        }
        return list;
    }

    @Override
    public MembershipPlanDTO getPlanById(Long id) {
        try {
            Optional<MembershipPlan> optional = membershipPlanRepository.findById(id);
            if (optional.isPresent()) {
                MembershipPlan p = optional.get();
                log.info("Specific membership plan retrieve successfully from database.");
                return new MembershipPlanDTO(p.getId(), p.getPlanName(), p.getPrice(), p.getDurationDays(), p.getDiscountPercentage());
            }
        } catch (Exception e) {
            log.error("Specific membership plan couldn't find", e.getMessage());
        }
        return null;
    }
}
