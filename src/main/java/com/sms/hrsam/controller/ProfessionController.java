package com.sms.hrsam.controller;

import com.sms.hrsam.dto.ProfessionDTO;
import com.sms.hrsam.entity.Profession;
import com.sms.hrsam.service.ProfessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfessionController {
    private final ProfessionService professionService;

    @GetMapping
    public ResponseEntity<List<ProfessionDTO>> getAllProfessions() {
        return ResponseEntity.ok(professionService.getAllProfessions());
    }

    @GetMapping("/industry/{industryId}")
    public ResponseEntity<List<ProfessionDTO>> getProfessionsByIndustry(@PathVariable Long industryId) {
        return ResponseEntity.ok(professionService.getProfessionsByIndustry(industryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionDTO> getProfessionById(@PathVariable Long id) {
        return ResponseEntity.ok(professionService.getProfessionById(id));
    }

    @PostMapping
    public ResponseEntity<ProfessionDTO> createProfession(@Valid @RequestBody ProfessionDTO professionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(professionService.createProfession(professionDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionDTO> updateProfession(
            @PathVariable Long id,
            @Valid @RequestBody ProfessionDTO professionDTO
    ) {
        return ResponseEntity.ok(professionService.updateProfession(id, professionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfession(@PathVariable Long id) {
        professionService.deleteProfession(id);
        return ResponseEntity.noContent().build();
    }
}