package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.dto.MembershipPlanDTO;

import java.util.List;

public interface MembershipPlanService {
    void savePlan(MembershipPlanDTO dto);
    void updatePlan(MembershipPlanDTO dto);
    void deletePlan(Long id);
    List<MembershipPlanDTO> getAllPlans();
    MembershipPlanDTO getPlanById(Long id);
    void restorePlan(Long id);
    List<MembershipPlanDTO> getInactivePlans();
}
