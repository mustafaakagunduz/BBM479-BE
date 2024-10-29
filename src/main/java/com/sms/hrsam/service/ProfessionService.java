package com.sms.hrsam.service;

import com.sms.hrsam.entity.Profession;
import com.sms.hrsam.exception.ResourceNotFoundException;
import com.sms.hrsam.repository.ProfessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfessionService {

    private final ProfessionRepository professionRepository;

    @Autowired
    public ProfessionService(ProfessionRepository professionRepository) {
        this.professionRepository = professionRepository;
    }

    public Profession createProfession(Profession profession) {
        return professionRepository.save(profession);
    }

    public Profession getProfession(Long professionId) {
        return professionRepository.findById(professionId)
                .orElseThrow(() -> new ResourceNotFoundException("Profession not found with id: " + professionId));
    }

    public List<Profession> getAllProfessions() {
        return professionRepository.findAll();
    }
}
