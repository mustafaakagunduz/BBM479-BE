package com.sms.hrsam.controller;

import com.sms.hrsam.dto.*;
import com.sms.hrsam.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SurveyController {
    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<SurveyDTO> createSurvey(@RequestBody SurveyDTO surveyDTO) {
        return ResponseEntity.ok(surveyService.createSurvey(surveyDTO));
    }

    @GetMapping
    public ResponseEntity<List<SurveyDTO>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDTO> getSurveyById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SurveyDTO>> getSurveysByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(surveyService.getSurveysByUserId(userId));
    }

    @PostMapping("/{surveyId}/responses")
    public ResponseEntity<Void> submitSurveyResponse(
            @PathVariable Long surveyId,
            @RequestBody SurveyResponseDTO responseDTO) {
        surveyService.submitSurveyResponse(surveyId, responseDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{surveyId}/responses")
    public ResponseEntity<List<SurveyResponseDTO>> getSurveyResponses(
            @PathVariable Long surveyId) {
        return ResponseEntity.ok(surveyService.getSurveyResponses(surveyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyDTO> updateSurvey(@PathVariable Long id, @RequestBody SurveyDTO surveyDTO) {
        return ResponseEntity.ok(surveyService.updateSurvey(id, surveyDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        try {
            surveyService.deleteSurvey(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}