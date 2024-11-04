package com.sms.hrsam.service;

import com.sms.hrsam.dto.ProfessionDTO;
import java.util.List;

public interface ProfessionService {
    List<ProfessionDTO> getAllProfessions();
    ProfessionDTO getProfessionById(Long id);
    ProfessionDTO createProfession(ProfessionDTO professionDTO);
    ProfessionDTO updateProfession(Long id, ProfessionDTO professionDTO);
    void deleteProfession(Long id);
     List<ProfessionDTO> getProfessionsByIndustry(Long industryId);
}
