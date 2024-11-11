package com.sms.hrsam.service;

import com.sms.hrsam.dto.AnswerDTO;
import com.sms.hrsam.dto.SurveyResponseCreateDTO;
import com.sms.hrsam.entity.*;
import com.sms.hrsam.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;

    @Autowired
    public ResponseService(
            ResponseRepository responseRepository,
            SurveyRepository surveyRepository,
            QuestionRepository questionRepository,
            UserRepository userRepository,
            OptionRepository optionRepository
    ) {
        this.responseRepository = responseRepository;
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public void createResponses(SurveyResponseCreateDTO responseDTO) {
        User user = userRepository.findById(responseDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Survey survey = surveyRepository.findById(responseDTO.getSurveyId())
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        for (AnswerDTO answerDTO : responseDTO.getAnswers()) {
            Question question = questionRepository.findById(answerDTO.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            // Find the appropriate option based on the level
            Option option = question.getOptions().stream()
                    .filter(opt -> opt.getLevel().equals(answerDTO.getSelectedLevel()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Option not found for level: " + answerDTO.getSelectedLevel()));

            Response response = new Response();
            response.setUser(user);
            response.setSurvey(survey);
            response.setQuestion(question);
            response.setOption(option);
            response.setEnteredLevel(answerDTO.getSelectedLevel());

            responseRepository.save(response);
        }
    }

    public List<Response> getResponsesBySurveyId(Long surveyId) {
        return responseRepository.findBySurveyId(surveyId);
    }

    public Response createResponse(Response response) {
        return responseRepository.save(response);
    }
}