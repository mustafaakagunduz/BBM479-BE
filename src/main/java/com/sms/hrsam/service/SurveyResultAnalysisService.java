package com.sms.hrsam.service;

import com.sms.hrsam.entity.Profession;
import com.sms.hrsam.entity.Response;
import com.sms.hrsam.entity.SurveyAnalysisRequest;
import com.sms.hrsam.entity.SurveyResultAnalysis;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.SurveyResultAnalysisRepository;
import com.sms.hrsam.repository.SurveyResultRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class SurveyResultAnalysisService {
    private final SurveyResultAnalysisRepository analysisRepository;
    private final SurveyResultRepository surveyResultRepository;

    @Autowired
    public SurveyResultAnalysisService(
            SurveyResultAnalysisRepository analysisRepository,
            SurveyResultRepository surveyResultRepository
    ) {
        this.analysisRepository = analysisRepository;
        this.surveyResultRepository = surveyResultRepository;
    }

    @Transactional
    public SurveyResultAnalysis saveAnalysis(Long surveyResultId, SurveyAnalysisRequest request) {
        // Önce survey result'un var olduğunu kontrol et
        surveyResultRepository.findById(surveyResultId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey result not found"));

        // Mevcut analizi bul veya yeni oluştur
        SurveyResultAnalysis analysis = analysisRepository
                .findBySurveyResultId(surveyResultId)
                .orElse(new SurveyResultAnalysis());

        // Değerleri güncelle
        analysis.setSurveyResultId(surveyResultId);
        analysis.setAnalysisText(request.getAnalysisText());
        analysis.setRecommendations(request.getRecommendations() != null ?
                request.getRecommendations() : new ArrayList<>());

        return analysisRepository.save(analysis);
    }

    public SurveyResultAnalysis getAnalysis(Long surveyResultId) {
        return analysisRepository.findBySurveyResultId(surveyResultId)
                .orElseThrow(() -> new ResourceNotFoundException("Analysis not found"));
    }
}