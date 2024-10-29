package com.sms.hrsam.controller;

import com.sms.hrsam.dto.SurveyCreateDTO;
import com.sms.hrsam.entity.Survey;
import com.sms.hrsam.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping
    public List<Survey> getAllSurveys() {
        return surveyService.getAllSurveys();
    }

    @PostMapping
    public Survey createSurvey(@RequestBody SurveyCreateDTO surveyDTO, @RequestParam Long userId) {
        return surveyService.createSurvey(surveyDTO, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id) {
        Survey survey = surveyService.getSurvey(id);
        return ResponseEntity.ok(survey);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Survey> updateSurvey(@PathVariable Long id, @RequestBody SurveyCreateDTO surveyDTO) {
        Survey updatedSurvey = surveyService.updateSurvey(id, surveyDTO);
        return ResponseEntity.ok(updatedSurvey);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }
}
