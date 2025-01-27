package com.sms.hrsam.service;

import com.sms.hrsam.dto.CompanySkillAnalysisDTO;
import com.sms.hrsam.dto.SkillDetailDTO;
import com.sms.hrsam.entity.*;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.CompanyRepository;
import com.sms.hrsam.repository.ResponseRepository;
import com.sms.hrsam.repository.SurveyRepository;
import com.sms.hrsam.repository.SurveyResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyAnalysisService {
    private final SurveyResultRepository surveyResultRepository;
    private final CompanyRepository companyRepository;
    private final SurveyRepository surveyRepository;
    private final ResponseRepository responseRepository;

    public CompanyAnalysisService(
            SurveyResultRepository surveyResultRepository,
            CompanyRepository companyRepository,
            SurveyRepository surveyRepository,
            ResponseRepository responseRepository) {
        this.surveyResultRepository = surveyResultRepository;
        this.companyRepository = companyRepository;
        this.surveyRepository = surveyRepository;
        this.responseRepository = responseRepository;
    }

    public List<SkillDetailDTO> getSkillDetails(Long companyId, Long surveyId, String skillName) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        List<Response> responses = responseRepository.findByCompanyIdAndSurveyIdAndSkillName(
                companyId, surveyId, skillName);

        return responses.stream()
                .map(response -> new SkillDetailDTO(
                        response.getUser().getId(),
                        response.getUser().getName(),
                        response.getEnteredLevel()))
                .collect(Collectors.toList());
    }

    public CompanySkillAnalysisDTO analyzeCompanySkills(Long companyId, Long surveyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        Survey survey = surveyRepository.findByIdWithQuestionsAndSkills(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found"));

        Map<String, List<Integer>> skillScores = new HashMap<>();
        survey.getQuestions().forEach(q ->
                skillScores.put(q.getSkill().getName(), new ArrayList<>())
        );

        // Şirket çalışanlarının cevaplarını al
        List<User> companyUsers = company.getUsers();
        for (User user : companyUsers) {
            List<Response> userResponses = responseRepository.findBySurveyIdAndUserId(surveyId, user.getId());
            for (Response response : userResponses) {
                String skillName = response.getQuestion().getSkill().getName();
                skillScores.get(skillName).add(response.getEnteredLevel());
            }
        }

        List<CompanySkillAnalysisDTO.SkillScore> averageScores = skillScores.entrySet().stream()
                .map(entry -> {
                    double average = entry.getValue().isEmpty() ? 0.0 :
                            entry.getValue().stream()
                                    .mapToDouble(Integer::doubleValue)
                                    .average()
                                    .orElse(0.0);
                    return new CompanySkillAnalysisDTO.SkillScore(entry.getKey(), average);
                })
                .collect(Collectors.toList());

        return new CompanySkillAnalysisDTO(companyId, company.getName(), averageScores);
    }
}