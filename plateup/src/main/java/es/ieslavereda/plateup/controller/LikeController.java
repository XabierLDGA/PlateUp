package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.model.Like;
import es.ieslavereda.plateup.model.LikeId;
import es.ieslavereda.plateup.model.Recipe;
import es.ieslavereda.plateup.repository.LikeRepository;
import es.ieslavereda.plateup.repository.RecipeRepository;
import es.ieslavereda.plateup.service.AchievementUnlockService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
public class LikeController {

    private final LikeRepository repository;
    private final RecipeRepository recipeRepository;
    private final AchievementUnlockService achievementUnlockService;

    public LikeController(
            LikeRepository repository,
            RecipeRepository recipeRepository,
            AchievementUnlockService achievementUnlockService
    ) {
        this.repository = repository;
        this.recipeRepository = recipeRepository;
        this.achievementUnlockService = achievementUnlockService;
    }

    @GetMapping
    public List<Like> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{userId}/{recipeId}")
    public Optional<Like> getById(@PathVariable Long userId, @PathVariable Long recipeId) {
        return repository.findById(new LikeId(userId, recipeId));
    }

    @PostMapping
    public Like create(@RequestBody Like like) {
        if (like.getCreatedAt() == null) {
            like.setCreatedAt(LocalDateTime.now());
        }

        Like savedLike = repository.save(like);

        recipeRepository.findById(savedLike.getRecipeId())
                .map(Recipe::getUserId)
                .ifPresent(achievementUnlockService::checkLikeAchievements);

        return savedLike;
    }

    @DeleteMapping("/{userId}/{recipeId}")
    public void delete(@PathVariable Long userId, @PathVariable Long recipeId) {
        repository.deleteById(new LikeId(userId, recipeId));
    }
}