package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.exception.AuthenticationRequiredException;
import es.ieslavereda.plateup.model.Like;
import es.ieslavereda.plateup.model.LikeId;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.LikeRepository;
import es.ieslavereda.plateup.repository.RecipeRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import es.ieslavereda.plateup.service.AchievementUnlockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
public class LikeController {

    private final LikeRepository repository;
    private final RecipeRepository recipeRepository;
    private final AchievementUnlockService achievementUnlockService;
    private final UserRepository userRepository;

    public LikeController(
            LikeRepository repository,
            RecipeRepository recipeRepository,
            AchievementUnlockService achievementUnlockService,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.recipeRepository = recipeRepository;
        this.achievementUnlockService = achievementUnlockService;
        this.userRepository = userRepository;
    }

    // Devuelve todos los likes de la aplicación
    @GetMapping
    public List<Like> getAll() {
        return repository.findAll();
    }

    // Devuelve todos los likes de una receta concreta
    @GetMapping("/recipe/{recipeId}")
    public List<Like> getByRecipeId(@PathVariable Long recipeId) {
        return repository.findByRecipeId(recipeId);
    }

    // Devuelve todos los likes que ha dado un usuario
    @GetMapping("/user/{userId}")
    public List<Like> getByUserId(@PathVariable Long userId) {
        return repository.findByUserId(userId);
    }

    // Devuelve el número de likes agrupado por receta para una lista de IDs
    @GetMapping("/counts")
    public Map<Long, Long> getCounts(@RequestParam List<Long> recipeIds) {
        if (recipeIds == null || recipeIds.isEmpty()) return Map.of();
        return repository.countGroupByRecipeId(recipeIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
    }

    // Comprueba si un usuario concreto ha dado like a una receta concreta
    @GetMapping("/{userId}/{recipeId}")
    public Optional<Like> getById(@PathVariable Long userId, @PathVariable Long recipeId) {
        return repository.findById(new LikeId(userId, recipeId));
    }

    // Registra un like del usuario autenticado y comprueba si el autor de la receta desbloquea algún logro
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Like like) {
        User authenticatedUser = getAuthenticatedUser();

        like.setUserId(authenticatedUser.getId());

        if (like.getCreatedAt() == null) {
            like.setCreatedAt(LocalDateTime.now());
        }

        Like savedLike = repository.save(like);

        // Buscamos al autor de la receta para comprobarle los logros de likes recibidos
        recipeRepository.findUserIdById(savedLike.getRecipeId())
                .ifPresent(achievementUnlockService::checkLikeAchievements);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedLike);
    }

    // Elimina un like; solo lo puede quitar el usuario que lo dio
    @DeleteMapping("/{userId}/{recipeId}")
    public ResponseEntity<?> delete(@PathVariable Long userId, @PathVariable Long recipeId) {
        User authenticatedUser = getAuthenticatedUser();

        if (!authenticatedUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only delete your own likes.");
        }

        repository.deleteById(new LikeId(userId, recipeId));
        return ResponseEntity.ok().build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationRequiredException("Authenticated user not found");
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AuthenticationRequiredException("Authenticated user not found"));
    }
}