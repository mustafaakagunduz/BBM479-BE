package com.sms.hrsam.controller;

import com.sms.hrsam.dto.SurveyResponseCreateDTO;
import com.sms.hrsam.entity.Response;
import com.sms.hrsam.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
@CrossOrigin(origins = "*")
public class ResponseController {
    private final ResponseService responseService;

    @Autowired
    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @PostMapping
    public ResponseEntity<Void> createResponses(@RequestBody SurveyResponseCreateDTO responseDTO) {
        responseService.createResponses(responseDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<Response>> getResponsesBySurveyId(@PathVariable Long surveyId) {
        return ResponseEntity.ok(responseService.getResponsesBySurveyId(surveyId));
    }
}