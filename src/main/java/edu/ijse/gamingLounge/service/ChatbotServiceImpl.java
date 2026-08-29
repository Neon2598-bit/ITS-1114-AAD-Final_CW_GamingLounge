package edu.ijse.gamingLounge.service;

import edu.ijse.gamingLounge.entity.*;
import edu.ijse.gamingLounge.repository.*;
import edu.ijse.gamingLounge.status.StationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotServiceImpl implements ChatbotService {
    private final StationTypeRepository stationTypeRepository;
    private final GameRepository gameRepository;
    private final SnackRepository snackRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final StationRepository stationRepository;
    private final BranchRepository branchRepository;

    private static final String FALLBACK_REPLY =
            "Sorry, I can only help with questions about our gaming lounge - station rates, " +
            "game availability, snacks, bookings, membership plans, branches, and opening hours. " +
            "Could you rephrase your question?";

    @Override
    public String getReply(String message) {
        String msg = message.toLowerCase();
        log.info("Chatbot received message: {}", message);

        if (msg.contains("rate") || msg.contains("price") || msg.contains("cost") || msg.contains("hourly")) {
            return getStationRatesReply();
        }

        if (msg.contains("available") || msg.contains("free station") || msg.contains("empty")) {
            return getAvailabilityReply();
        }

        if (msg.contains("game") || msg.contains("fifa") || msg.contains("play")) {
            return getGamesReply();
        }

        if (msg.contains("snack") || msg.contains("food") || msg.contains("drink") || msg.contains("eat")) {
            return getSnacksReply();
        }

        if (msg.contains("membership") || msg.contains("plan") || msg.contains("subscribe")) {
            return getMembershipReply();
        }

        if (msg.contains("book") || msg.contains("reserve") || msg.contains("reservation")) {
            return "To book a station: register an account, browse available stations, pick a " +
                    "date and time, and confirm payment. Your station will be marked reserved " +
                    "for you once the booking is confirmed.";
        }

        if (msg.contains("open") || msg.contains("close") || msg.contains("hours")) {
            return "We're open every day from 9:00 AM to 11:00 PM.";
        }

        if (msg.contains("branch") || msg.contains("location") || msg.contains("address") || msg.contains("where")) {
            return getBranchReply();
        }

        log.info("No matching category - returning fallback reply");
        return FALLBACK_REPLY;
    }

    private String getStationRatesReply() {
        List<StationType> types = stationTypeRepository.findByActiveTrue();
        if (types.isEmpty()) return "We don't have station pricing set up yet.";
        String list = types.stream()
                .map(t -> t.getTypeName() + ": Rs. " + t.getHourlyRate() + "/hour")
                .collect(Collectors.joining(", "));
        return "Here are our station rates - " + list + ".";
    }

    private String getAvailabilityReply() {
        long availableCount = stationRepository.findAll().stream()
                .filter(s -> s.isActive() && s.getStatus() == StationStatus.AVAILABLE)
                .count();
        return "We currently have " + availableCount + " station(s) available for booking.";
    }

    private String getGamesReply() {
        List<Game> games = gameRepository.findByActiveTrue();
        if (games.isEmpty()) return "We don't have any games listed yet.";
        String list = games.stream().map(Game::getGameName).collect(Collectors.joining(", "));
        return "Games available across our stations include: " + list + ".";
    }

    private String getSnacksReply() {
        List<Snack> snacks = snackRepository.findAllWithCategory();
        if (snacks.isEmpty()) return "We don't have any snacks listed yet.";
        String list = snacks.stream()
                .map(s -> s.getName() + " (Rs. " + s.getPrice() + ")")
                .collect(Collectors.joining(", "));
        return "Here's what's on our snack menu - " + list + ".";
    }

    private String getMembershipReply() {
        List<MembershipPlan> plans = membershipPlanRepository.findAllOrderByPriceNative();
        if (plans.isEmpty()) return "We don't have any membership plans set up yet.";
        String list = plans.stream()
                .map(p -> p.getPlanName() + " (Rs. " + p.getPrice() + " for " + p.getDurationDays() + " days)")
                .collect(Collectors.joining(", "));
        return "Our membership plans - " + list + ".";
    }

    private String getBranchReply() {
        List<Branch> branches = branchRepository.findByActiveTrue();
        if (branches.isEmpty()) return "We don't have any branches listed yet.";
        String list = branches.stream()
                .map(b -> b.getBranchName() + " (" + b.getAddress() + ")")
                .collect(Collectors.joining(", "));
        return "Our branches - " + list + ".";
    }
}
