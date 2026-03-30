package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.model.CookedRecipe;
import es.ieslavereda.plateup.repository.CookedRecipeRepository;
import es.ieslavereda.plateup.service.AchievementUnlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cooked")
@CrossOrigin(origins = "*")
public class CookedRecipeController {

    private final CookedRecipeRepository repository;
    private final AchievementUnlockService achievementUnlockService;

    public CookedRecipeController(CookedRecipeRepository repository, AchievementUnlockService achievementUnlockService) {
        this.repository = repository;
        this.achievementUnlockService = achievementUnlockService;
    }

    @GetMapping("/user/{userId}")
    public List<CookedRecipe> getByUser(@PathVariable Long userId) {
        return repository.findByUserId(userId);
    }

    @GetMapping("/user/{userId}/recipe/{recipeId}")
    public ResponseEntity<CookedRecipe> getEntry(
            @PathVariable Long userId,
            @PathVariable Long recipeId) {

        return repository.findByUserIdAndRecipeId(userId, recipeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CookedRecipe save(@RequestBody CookedRecipe payload) {
        CookedRecipe savedCookedRecipe = repository.findByUserIdAndRecipeId(payload.getUserId(), payload.getRecipeId())
                .map(existing -> {
                    existing.setCookedAt(LocalDateTime.now());
                    existing.setElapsedSeconds(payload.getElapsedSeconds());
                    return repository.save(existing);
                })
                .orElseGet(() -> {
                    payload.setCookedAt(LocalDateTime.now());
                    return repository.save(payload);
                });

        achievementUnlockService.checkCookedAchievements(savedCookedRecipe.getUserId());
        return savedCookedRecipe;
    }

    @DeleteMapping("/user/{userId}/recipe/{recipeId}")
    @Transactional
    public ResponseEntity<Void> delete(
            @PathVariable Long userId,
            @PathVariable Long recipeId) {

        if (!repository.existsByUserIdAndRecipeId(userId, recipeId)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteByUserIdAndRecipeId(userId, recipeId);
        return ResponseEntity.noContent().build();
    }
}