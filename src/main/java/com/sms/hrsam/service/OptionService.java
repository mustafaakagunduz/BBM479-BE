package com.sms.hrsam.service;

import com.sms.hrsam.entity.Option;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public List<Option> getAllOptions() {
        return optionRepository.findAll();
    }

    public Option getOptionById(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found with id: " + id));
    }

    public Option createOption(Option option) {
        return optionRepository.save(option);
    }

}
