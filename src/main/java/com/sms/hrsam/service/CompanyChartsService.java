// CompanyChartsService.java
package com.sms.hrsam.service;

import com.sms.hrsam.controller.CompanyChartsController;
import com.sms.hrsam.dto.CompanySkillStatsDTO;
import com.sms.hrsam.dto.UserSkillDTO;
import com.sms.hrsam.entity.Response;
import com.sms.hrsam.entity.RequiredLevel;
import com.sms.hrsam.entity.Skill;
import com.sms.hrsam.repository.CompanyChartsRepository;
import com.sms.hrsam.repository.RequiredLevelRepository;
import com.sms.hrsam.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyChartsService {
    private static final Logger log = LoggerFactory.getLogger(CompanyChartsController.class);

    private final CompanyChartsRepository companyChartsRepository;
    private final RequiredLevelRepository requiredLevelRepository;
    private final SkillRepository skillRepository;


    public List<CompanySkillStatsDTO> getAllSkillsStatsForCompany(Long companyId) {
        // Şirketin cevaplanan tüm skill'lerini bul
        List<Long> answeredSkillIds = companyChartsRepository
                .findDistinctSkillIdsByCompanyId(companyId);

        log.info("Found {} distinct skills for company {}", answeredSkillIds.size(), companyId);

        return answeredSkillIds.stream()
                .map(skillId -> getCompanySkillStats(companyId, skillId))
                .collect(Collectors.toList());
    }
    public CompanySkillStatsDTO getCompanySkillStats(Long companyId, Long skillId) {
        // Tüm yanıtları al
        List<Response> responses = companyChartsRepository
                .findAllResponsesByCompanyAndSkill(companyId, skillId);
        log.info("Found {} responses for company {} and skill {}",
                responses.size(), companyId, skillId);

        if (responses.isEmpty()) {
            return CompanySkillStatsDTO.builder()
                    .skillId(skillId)
                    .skillName(skillRepository.findById(skillId)
                            .map(Skill::getName).orElse("Unknown"))
                    .companyAverage(0.0)
                    .aboveAverageScore(0.0)
                    .belowAverageScore(0.0)
                    .aboveAverageUsers(new ArrayList<>())
                    .build();
        }

        // Kullanıcı bazında skill skorlarını hesapla
        Map<Long, List<Double>> userScores = new HashMap<>();
        for (Response response : responses) {
            Long userId = response.getUser().getId();
            int userLevel = response.getEnteredLevel();

            // Skill skoru hesaplama (percentage)
            double score = (double) userLevel * 20; // 1-5 arası değeri 0-100 arası değere çevir
            userScores.computeIfAbsent(userId, k -> new ArrayList<>()).add(score);
        }

        // Kullanıcı ortalamalarını hesapla
        Map<Long, Double> userAverages = userScores.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .mapToDouble(Double::doubleValue)
                                .average()
                                .orElse(0.0)
                ));

        // Şirket ortalamasını hesapla
        double companyAverage = userAverages.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        // Ortalama üstü ve altı kullanıcıları ayır
        List<UserSkillDTO> aboveAverageUsers = new ArrayList<>();
        List<Double> aboveAverageScores = new ArrayList<>();
        List<Double> belowAverageScores = new ArrayList<>();

        userAverages.forEach((userId, score) -> {
            if (score >= companyAverage) {
                aboveAverageScores.add(score);
                aboveAverageUsers.add(UserSkillDTO.builder()
                        .userId(userId)
                        .userName(responses.stream()
                                .filter(r -> r.getUser().getId().equals(userId))
                                .findFirst()
                                .map(r -> r.getUser().getName())
                                .orElse("Unknown"))
                        .skillScore(score)
                        .build());
            } else {
                belowAverageScores.add(score);
            }
        });

        log.info("Found {} above average users", aboveAverageUsers.size());

        // Ortalama üstü ve altı skorların ortalamalarını hesapla
        double aboveAverageScore = aboveAverageScores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double belowAverageScore = belowAverageScores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return CompanySkillStatsDTO.builder()
                .skillId(skillId)
                .skillName(responses.isEmpty() ? "" : responses.get(0).getQuestion().getSkill().getName())
                .companyAverage(companyAverage)
                .aboveAverageScore(aboveAverageScore)
                .belowAverageScore(belowAverageScore)
                .aboveAverageUsers(aboveAverageUsers)
                .build();
    }

    private double calculateSkillScore(int userLevel, int requiredLevel) {
        if (requiredLevel == 0) return 0.0;
        double score = (double) Math.min(userLevel, requiredLevel) / requiredLevel * 100;
        return Math.min(100.0, score); // Maximum 100
    }
}