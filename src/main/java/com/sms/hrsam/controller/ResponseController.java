package com.sms.hrsam.controller;

import com.sms.hrsam.entity.Response;
import com.sms.hrsam.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {

    private final ResponseService responseService;

    @Autowired
    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @PostMapping
    public Response createResponse(@RequestBody Response response) {
        return responseService.createResponse(response);
    }

    @GetMapping("/survey/{surveyId}")
    public List<Response> getResponsesBySurveyId(@PathVariable Long surveyId) {
        return responseService.getResponsesBySurveyId(surveyId);
    }
}
