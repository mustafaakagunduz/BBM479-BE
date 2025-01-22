package com.sms.hrsam.service;

import com.sms.hrsam.dto.AnswerDTO;
import com.sms.hrsam.dto.SurveyResponseCreateDTO;
import com.sms.hrsam.entity.*;
import com.sms.hrsam.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
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

        // Son attempt number'ı bul
        Integer lastAttemptNumber = responseRepository.findLastAttemptNumber(
                responseDTO.getUserId(),
                responseDTO.getSurveyId()
        ).orElse(0);

        // Yeni attempt number
        Integer currentAttemptNumber = lastAttemptNumber + 1;

        LocalDateTime now = LocalDateTime.now();

        // Tüm yanıtları tek seferde kaydet
        List<Response> responses = responseDTO.getAnswers().stream()
                .map(answerDTO -> {
                    Question question = questionRepository.findById(answerDTO.getQuestionId())
                            .orElseThrow(() -> new RuntimeException("Question not found"));

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
                    response.setCreatedAt(now);
                    response.setAttemptNumber(currentAttemptNumber); // Her yanıt için aynı attempt number
                    return response;
                })
                .collect(Collectors.toList());

        responseRepository.saveAll(responses);
    }

    public boolean isSurveyCompletedByUser(Long surveyId, Long userId) {
        try {
            // Anketteki soruları al
            List<Question> questions = questionRepository.findBySurveyId(surveyId);
            Set<Long> questionIds = questions.stream()
                    .map(Question::getId)
                    .collect(Collectors.toSet());

            // Kullanıcının cevaplarını al
            List<Response> responses = responseRepository.findBySurveyIdAndUserId(surveyId, userId);

            // Her benzersiz soru için son cevabı al
            Map<Long, Response> latestResponses = responses.stream()
                    .collect(Collectors.groupingBy(
                            response -> response.getQuestion().getId(),
                            Collectors.collectingAndThen(
                                    Collectors.maxBy(Comparator.comparing(Response::getId)),
                                    optional -> optional.orElse(null)
                            )
                    ));

            // Her sorunun cevaplanıp cevaplanmadığını kontrol et
            boolean allQuestionsAnswered = questionIds.stream()
                    .allMatch(qId -> latestResponses.containsKey(qId) &&
                            latestResponses.get(qId).getEnteredLevel() != null);

            log.info("Survey completion check - Total Questions: {}, Unique Answered Questions: {}, All Questions Answered: {}",
                    questionIds.size(), latestResponses.size(), allQuestionsAnswered);

            return allQuestionsAnswered;

        } catch (Exception e) {
            log.error("Error checking survey completion:", e);
            return false;
        }
    }

    public List<Response> getResponsesBySurveyId(Long surveyId) {
        return responseRepository.findBySurveyId(surveyId);
    }

    public Response createResponse(Response response) {
        return responseRepository.save(response);
    }
}