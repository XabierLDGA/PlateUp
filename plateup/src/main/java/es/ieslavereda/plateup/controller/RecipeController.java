package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.model.Recipe;
import es.ieslavereda.plateup.repository.RecipeRepository;
import es.ieslavereda.plateup.service.AchievementUnlockService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeRepository repository;
    private final AchievementUnlockService achievementUnlockService;

    public RecipeController(RecipeRepository repository, AchievementUnlockService achievementUnlockService) {
        this.repository = repository;
        this.achievementUnlockService = achievementUnlockService;
    }

    @GetMapping
    public List<Recipe> getAll() {
        return repository.findAllByOrderByCreatedAtDescIdDesc();
    }

    @GetMapping("/{id}")
    public Optional<Recipe> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Recipe> getByUserId(@PathVariable Long userId) {
        return repository.findByUserIdOrderByCreatedAtDescIdDesc(userId);
    }

    @PostMapping
    public Recipe create(@RequestBody Recipe recipe) {
        LocalDateTime now = LocalDateTime.now();

        if (recipe.getCreatedAt() == null) {
            recipe.setCreatedAt(now);
        }

        recipe.setUpdatedAt(now);

        Recipe savedRecipe = repository.save(recipe);
        achievementUnlockService.checkRecipeAchievements(savedRecipe.getUserId());
        return savedRecipe;
    }

    @PutMapping("/{id}")
    public Recipe update(@PathVariable Long id, @RequestBody Recipe recipe) {
        Recipe existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        recipe.setId(existing.getId());

        if (recipe.getCreatedAt() == null) {
            recipe.setCreatedAt(existing.getCreatedAt());
        }

        recipe.setUpdatedAt(LocalDateTime.now());
        return repository.save(recipe);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}