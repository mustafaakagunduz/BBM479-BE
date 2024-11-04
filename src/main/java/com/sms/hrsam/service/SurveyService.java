package com.sms.hrsam.service;

import com.sms.hrsam.dto.*;
import com.sms.hrsam.entity.*;
import com.sms.hrsam.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final ResponseRepository responseRepository;
    private final UserRepository userRepository;
    private final IndustryRepository industryRepository;
    private final SkillRepository skillRepository;
    private final ProfessionRepository professionRepository;

    @Transactional
    public SurveyDTO createSurvey(SurveyDTO surveyDTO) {
        // Get user
        User user = userRepository.findById(surveyDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get industry
        Industry industry = industryRepository.findById(surveyDTO.getIndustryId())
                .orElseThrow(() -> new RuntimeException("Industry not found"));

        // Get professions
        List<Profession> professions = professionRepository.findAllById(surveyDTO.getSelectedProfessions());
        if (professions.size() != surveyDTO.getSelectedProfessions().size()) {
            throw new RuntimeException("Some professions were not found");
        }

        // Create and save survey
        Survey survey = new Survey();
        survey.setTitle(surveyDTO.getTitle());
        survey.setUser(user);
        survey.setIndustry(industry);
        survey.setProfessions(professions);

        Survey savedSurvey = surveyRepository.save(survey);

        // Create and save questions
        surveyDTO.getQuestions().forEach(questionDTO -> {
            Question question = new Question();
            question.setText(questionDTO.getText());
            question.setSurvey(savedSurvey);

            Skill skill = skillRepository.findById(questionDTO.getSkillId())
                    .orElseThrow(() -> new RuntimeException("Skill not found"));
            question.setSkill(skill);

            // Create options
            questionDTO.getOptions().forEach(optionDTO -> {
                Option option = new Option();
                option.setLevel(optionDTO.getLevel());
                option.setDescription(optionDTO.getDescription());
                option.setQuestion(question);
                question.getOptions().add(option);
            });

            questionRepository.save(question);
        });

        return mapToDTO(savedSurvey);
    }

    public List<SurveyDTO> getAllSurveys() {
        return surveyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SurveyDTO getSurveyById(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
        return mapToDTO(survey);
    }

    public List<SurveyDTO> getSurveysByUserId(Long userId) {
        return surveyRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void submitSurveyResponse(Long surveyId, SurveyResponseDTO responseDTO) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        responseDTO.getResponses().forEach(questionResponse -> {
            Response response = new Response();
            response.setSurvey(survey);

            Question question = questionRepository.findById(questionResponse.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            response.setQuestion(question);

            Option option = optionRepository.findById(questionResponse.getOptionId())
                    .orElseThrow(() -> new RuntimeException("Option not found"));
            response.setOption(option);

            response.setEnteredLevel(questionResponse.getSelectedLevel());

            responseRepository.save(response);
        });
    }

    public List<SurveyResponseDTO> getSurveyResponses(Long surveyId) {
        List<Response> responses = responseRepository.findBySurveyId(surveyId);
        // Implementation of response mapping logic
        return responses.stream()
                .collect(Collectors.groupingBy(response -> response.getSurvey().getUser().getId()))
                .entrySet().stream()
                .map(entry -> {
                    SurveyResponseDTO dto = new SurveyResponseDTO();
                    dto.setUserId(entry.getKey());
                    dto.setSurveyId(surveyId);
                    dto.setResponses(entry.getValue().stream()
                            .map(this::mapToQuestionResponse)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSurvey(Long id) {
        surveyRepository.deleteById(id);
    }

    private SurveyDTO mapToDTO(Survey survey) {
        SurveyDTO dto = new SurveyDTO();
        dto.setId(survey.getId());
        dto.setTitle(survey.getTitle());
        dto.setUserId(survey.getUser().getId());
        dto.setIndustryId(survey.getIndustry().getId());
        dto.setSelectedProfessions(
                survey.getProfessions().stream()
                        .map(Profession::getId)
                        .collect(Collectors.toList())
        );
        dto.setQuestions(
                survey.getQuestions().stream()
                        .map(this::mapToQuestionDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }


    private QuestionDTO mapToQuestionDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setText(question.getText());
        dto.setSkillId(question.getSkill().getId());
        dto.setOptions(question.getOptions().stream()
                .map(this::mapToOptionDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private OptionDTO mapToOptionDTO(Option option) {
        OptionDTO dto = new OptionDTO();
        dto.setId(option.getId());
        dto.setLevel(option.getLevel());
        dto.setDescription(option.getDescription());
        return dto;
    }

    private QuestionResponseDTO mapToQuestionResponse(Response response) {
        QuestionResponseDTO dto = new QuestionResponseDTO();
        dto.setQuestionId(response.getQuestion().getId());
        dto.setOptionId(response.getOption().getId());
        dto.setSelectedLevel(response.getEnteredLevel());
        return dto;
    }
}