// IndustryController.java
package com.sms.hrsam.controller;

import com.sms.hrsam.dto.IndustryDTO;
import com.sms.hrsam.service.IndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/industries")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IndustryController {

    private final IndustryService industryService;

    @GetMapping
    public ResponseEntity<List<IndustryDTO>> getAllIndustries() {
        return ResponseEntity.ok(industryService.getAllIndustries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndustryDTO> getIndustryById(@PathVariable Long id) {
        return ResponseEntity.ok(industryService.getIndustryById(id));
    }

    @PostMapping
    public ResponseEntity<IndustryDTO> createIndustry(@RequestBody IndustryDTO industryDTO) {
        return ResponseEntity.ok(industryService.createIndustry(industryDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndustryDTO> updateIndustry(
            @PathVariable Long id,
            @RequestBody IndustryDTO industryDTO
    ) {
        return ResponseEntity.ok(industryService.updateIndustry(id, industryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndustry(@PathVariable Long id) {
        industryService.deleteIndustry(id);
        return ResponseEntity.ok().build();
    }
}