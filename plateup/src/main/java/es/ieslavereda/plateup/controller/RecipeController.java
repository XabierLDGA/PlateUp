package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.exception.AuthenticationRequiredException;
import es.ieslavereda.plateup.exception.ResourceNotFoundException;
import es.ieslavereda.plateup.model.Recipe;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.RecipeRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import es.ieslavereda.plateup.service.AchievementUnlockService;
import es.ieslavereda.plateup.service.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeRepository repository;
    private final UserRepository userRepository;
    private final AchievementUnlockService achievementUnlockService;
    private final FileStorageService fileStorageService;

    public RecipeController(
            RecipeRepository repository,
            UserRepository userRepository,
            AchievementUnlockService achievementUnlockService,
            FileStorageService fileStorageService
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.achievementUnlockService = achievementUnlockService;
        this.fileStorageService = fileStorageService;
    }

    // Devuelve todas las recetas ordenadas por fecha de creación descendente
    @GetMapping
    public List<Recipe> getAll() {
        return repository.findAllByOrderByCreatedAtDescIdDesc();
    }

    // Devuelve las recetas del feed del usuario autenticado, paginadas y con filtro opcional por categoría
    @GetMapping("/feed")
    public ResponseEntity<Page<Recipe>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category
    ) {
        Long userId = getAuthenticatedUser().getId();
        PageRequest pageable = PageRequest.of(page, size);
        Page<Recipe> result = (category != null && !category.isBlank())
                ? repository.findFeedForUserAndCategory(userId, category, pageable)
                : repository.findFeedForUser(userId, pageable);
        return ResponseEntity.ok(result);
    }

    // Devuelve el número total de recetas publicadas por un usuario
    @GetMapping("/count/{userId}")
    public long countByUser(@PathVariable Long userId) {
        return repository.countByUserId(userId);
    }

    // Devuelve varias recetas a la vez dado un listado de IDs
    @GetMapping("/by-ids")
    public List<Recipe> getByIds(@RequestParam List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return repository.findByIdIn(ids);
    }

    // Devuelve una receta concreta por su identificador
    @GetMapping("/{id}")
    public Optional<Recipe> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // Devuelve todas las recetas de un usuario ordenadas por fecha descendente
    @GetMapping("/user/{userId}")
    public List<Recipe> getByUserId(@PathVariable Long userId) {
        return repository.findByUserIdOrderByCreatedAtDescIdDesc(userId);
    }

    // Publica una nueva receta y comprueba si el autor desbloquea algún logro por publicaciones
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Recipe recipe) {
        User authenticatedUser = getAuthenticatedUser();
        LocalDateTime now = LocalDateTime.now();

        recipe.setUserId(authenticatedUser.getId());

        if (recipe.getCreatedAt() == null) {
            recipe.setCreatedAt(now);
        }

        recipe.setUpdatedAt(now);

        Recipe savedRecipe = repository.save(recipe);
        achievementUnlockService.checkRecipeAchievements(savedRecipe.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

    // Actualiza una receta existente; solo la puede editar su autor
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Recipe recipe) {
        Recipe existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        User authenticatedUser = getAuthenticatedUser();
        if (!existing.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only update your own recipes.");
        }

        String previousImageUrl = existing.getImageUrl();

        recipe.setId(existing.getId());
        recipe.setUserId(existing.getUserId());

        if (recipe.getCreatedAt() == null) {
            recipe.setCreatedAt(existing.getCreatedAt());
        }

        recipe.setUpdatedAt(LocalDateTime.now());
        Recipe updatedRecipe = repository.save(recipe);

        // Si la imagen ha cambiado, borramos la anterior del almacenamiento para no acumular archivos huérfanos
        if (!Objects.equals(previousImageUrl, updatedRecipe.getImageUrl())) {
            fileStorageService.deleteIfManagedPath(previousImageUrl);
        }

        return ResponseEntity.ok(updatedRecipe);
    }

    // Elimina una receta y su imagen asociada; solo lo puede hacer su autor
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Recipe existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        User authenticatedUser = getAuthenticatedUser();
        if (!existing.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only delete your own recipes.");
        }

        String imageToDelete = existing.getImageUrl();

        repository.deleteById(id);
        fileStorageService.deleteIfManagedPath(imageToDelete);

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