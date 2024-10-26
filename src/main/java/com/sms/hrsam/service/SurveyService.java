package com.sms.hrsam.service;
import com.sms.hrsam.dto.SurveyCreateDTO; // SurveyCreateDTO için
import com.sms.hrsam.entity.*; // Survey, User, Question, Option, Skill için
import com.sms.hrsam.exception.ResourceNotFoundException; // ResourceNotFoundException için
import com.sms.hrsam.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Service class
@Service
@Transactional
@Slf4j
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final SkillRepository skillRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository,
                         QuestionRepository questionRepository,
                         SkillRepository skillRepository,
                         OptionRepository optionRepository,
                         UserRepository userRepository) {
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.skillRepository = skillRepository;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
    }
    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll(); // Fetch all surveys from the repository
    }
    public Survey createSurvey(SurveyCreateDTO surveyDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Survey survey = new Survey();
        survey.setUser(user);

        List<Question> questions = surveyDTO.getQuestions().stream()
                .map(questionDTO -> {
                    Question question = new Question();
                    question.setText(questionDTO.getText());

                    Skill skill = skillRepository.findById(questionDTO.getSkillId())
                            .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
                    question.setSkill(skill);
                    question.setSurvey(survey);

                    List<Option> options = questionDTO.getOptions().stream()
                            .map(optionDTO -> {
                                Option option = new Option();
                                option.setLevel(optionDTO.getLevel());
                                option.setDescription(optionDTO.getDescription());
                                option.setQuestion(question);
                                return option;
                            })
                            .collect(Collectors.toList());

                    question.setOptions(options);
                    return question;
                })
                .collect(Collectors.toList());

        survey.setQuestions(questions);
        return surveyRepository.save(survey);
    }
}
