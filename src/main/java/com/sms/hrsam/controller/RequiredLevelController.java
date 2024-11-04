package com.sms.hrsam.controller;

import com.sms.hrsam.entity.RequiredLevel;
import com.sms.hrsam.service.RequiredLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/required-levels")
public class RequiredLevelController {

    private final RequiredLevelService requiredLevelService;

    @Autowired
    public RequiredLevelController(RequiredLevelService requiredLevelService) {
        this.requiredLevelService = requiredLevelService;
    }

    @PostMapping
    public ResponseEntity<RequiredLevel> createRequiredLevel(@Valid @RequestBody RequiredLevel requiredLevel) {
        RequiredLevel savedLevel = requiredLevelService.createRequiredLevel(requiredLevel);
        return ResponseEntity.ok(savedLevel);
    }

    @GetMapping
    public ResponseEntity<List<RequiredLevel>> getAllRequiredLevels() {
        List<RequiredLevel> levels = requiredLevelService.getAllRequiredLevels();
        return ResponseEntity.ok(levels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequiredLevel> getRequiredLevelById(@PathVariable Long id) {
        RequiredLevel level = requiredLevelService.getRequiredLevelById(id);
        return ResponseEntity.ok(level);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequiredLevel> updateRequiredLevel(
            @PathVariable Long id,
            @Valid @RequestBody RequiredLevel requiredLevel) {
        RequiredLevel updatedLevel = requiredLevelService.updateRequiredLevel(id, requiredLevel);
        return ResponseEntity.ok(updatedLevel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequiredLevel(@PathVariable Long id) {
        requiredLevelService.deleteRequiredLevel(id);
        return ResponseEntity.ok().build();
    }
}