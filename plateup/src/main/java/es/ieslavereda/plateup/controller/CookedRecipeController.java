package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.exception.AuthenticationRequiredException;
import es.ieslavereda.plateup.model.CookedRecipe;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.CookedRecipeRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import es.ieslavereda.plateup.service.AchievementUnlockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    public CookedRecipeController(
            CookedRecipeRepository repository,
            AchievementUnlockService achievementUnlockService,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.achievementUnlockService = achievementUnlockService;
        this.userRepository = userRepository;
    }

    // Devuelve todas las recetas que un usuario ha marcado como cocinadas
    @GetMapping("/user/{userId}")
    public List<CookedRecipe> getByUser(@PathVariable Long userId) {
        return repository.findByUserId(userId);
    }

    // Devuelve la entrada concreta de una receta cocinada por un usuario
    @GetMapping("/user/{userId}/recipe/{recipeId}")
    public ResponseEntity<CookedRecipe> getEntry(
            @PathVariable Long userId,
            @PathVariable Long recipeId) {

        return repository.findByUserIdAndRecipeId(userId, recipeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Registra que el usuario ha cocinado una receta; si ya existía, actualiza la fecha y el tiempo empleado
    @PostMapping
    public ResponseEntity<?> save(@RequestBody CookedRecipe payload) {
        User authenticatedUser = getAuthenticatedUser();
        Long authenticatedUserId = authenticatedUser.getId();

        // Si ya hay un registro previo se actualiza; si no, se crea uno nuevo
        CookedRecipe savedCookedRecipe = repository.findByUserIdAndRecipeId(authenticatedUserId, payload.getRecipeId())
                .map(existing -> {
                    existing.setCookedAt(LocalDateTime.now());
                    existing.setElapsedSeconds(payload.getElapsedSeconds());
                    return repository.save(existing);
                })
                .orElseGet(() -> {
                    payload.setUserId(authenticatedUserId);
                    payload.setCookedAt(LocalDateTime.now());
                    return repository.save(payload);
                });

        achievementUnlockService.checkCookedAchievements(savedCookedRecipe.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCookedRecipe);
    }

    // Elimina el registro de una receta cocinada; solo lo puede hacer el propio usuario
    @DeleteMapping("/user/{userId}/recipe/{recipeId}")
    @Transactional
    public ResponseEntity<?> delete(
            @PathVariable Long userId,
            @PathVariable Long recipeId) {

        User authenticatedUser = getAuthenticatedUser();

        if (!authenticatedUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only delete your own cooked recipes.");
        }

        if (!repository.existsByUserIdAndRecipeId(userId, recipeId)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteByUserIdAndRecipeId(userId, recipeId);
        return ResponseEntity.noContent().build();
    }

    // Obtiene el usuario autenticado a partir del contexto de seguridad de Spring
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationRequiredException("Authenticated user not found");
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AuthenticationRequiredException("Authenticated user not found"));
    }
}