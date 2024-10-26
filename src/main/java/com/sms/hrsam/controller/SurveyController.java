package com.sms.hrsam.controller;

import com.sms.hrsam.dto.SurveyCreateDTO;
import com.sms.hrsam.entity.Survey;
import com.sms.hrsam.service.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller class
@RestController
@RequestMapping("/api/surveys")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Slf4j
public class SurveyController {
    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    // Endpoint to create a new survey
    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody SurveyCreateDTO surveyDTO,
                                               @RequestParam Long userId) {
        log.info("Creating new survey for user: {}", userId);
        Survey survey = surveyService.createSurvey(surveyDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(survey);
    }

    // Endpoint to get all surveys
    @GetMapping
    public ResponseEntity<List<Survey>> getAllSurveys() {
        log.info("Fetching all surveys");
        List<Survey> surveys = surveyService.getAllSurveys();
        return ResponseEntity.ok(surveys);
    }
}
