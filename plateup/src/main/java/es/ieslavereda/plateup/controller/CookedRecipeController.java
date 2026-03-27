package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.model.CookedRecipe;
import es.ieslavereda.plateup.repository.CookedRecipeRepository;
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

    public CookedRecipeController(CookedRecipeRepository repository) {
        this.repository = repository;
    }

    /** All cooked entries for a specific user */
    @GetMapping("/user/{userId}")
    public List<CookedRecipe> getByUser(@PathVariable Long userId) {
        return repository.findByUserId(userId);
    }

    /** Check whether a specific user has already cooked a recipe */
    @GetMapping("/user/{userId}/recipe/{recipeId}")
    public ResponseEntity<CookedRecipe> getEntry(
            @PathVariable Long userId,
            @PathVariable Long recipeId) {

        return repository.findByUserIdAndRecipeId(userId, recipeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save (or update) a cooked-recipe entry.
     * Called when the user presses "Finish" on the cooking view.
     * Body must contain userId, recipeId, and optionally elapsedSeconds.
     */
    @PostMapping
    public CookedRecipe save(@RequestBody CookedRecipe payload) {
        // Upsert: if already exists, update elapsed time & timestamp
        return repository.findByUserIdAndRecipeId(payload.getUserId(), payload.getRecipeId())
                .map(existing -> {
                    existing.setCookedAt(LocalDateTime.now());
                    existing.setElapsedSeconds(payload.getElapsedSeconds());
                    return repository.save(existing);
                })
                .orElseGet(() -> {
                    payload.setCookedAt(LocalDateTime.now());
                    return repository.save(payload);
                });
    }

    /** Remove a cooked-recipe entry (un-cook) */
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