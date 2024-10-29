package com.sms.hrsam.service;

import com.sms.hrsam.entity.RequiredLevel;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.RequiredLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RequiredLevelService {

    private final RequiredLevelRepository requiredLevelRepository;

    @Autowired
    public RequiredLevelService(RequiredLevelRepository requiredLevelRepository) {
        this.requiredLevelRepository = requiredLevelRepository;
    }

    public RequiredLevel createRequiredLevel(RequiredLevel requiredLevel) {
        return requiredLevelRepository.save(requiredLevel);
    }

    public List<RequiredLevel> getAllRequiredLevels() {
        return requiredLevelRepository.findAll();
    }
}
