package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.exception.AuthenticationRequiredException;
import es.ieslavereda.plateup.model.Recipe;
import es.ieslavereda.plateup.model.RecipeStep;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.RecipeRepository;
import es.ieslavereda.plateup.repository.RecipeStepRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipesteps")
@CrossOrigin(origins = "*")
public class RecipeStepController {

    private final RecipeStepRepository recipeStepRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeStepController(
            RecipeStepRepository recipeStepRepository,
            RecipeRepository recipeRepository,
            UserRepository userRepository
    ) {
        this.recipeStepRepository = recipeStepRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    // Devuelve todos los pasos de una receta ordenados según el repositorio
    @GetMapping("/recipe/{recipeId}")
    public List<RecipeStep> getStepsByRecipe(@PathVariable Long recipeId) {
        return recipeStepRepository.findByRecipeId(recipeId);
    }

    // Añade un nuevo paso a una receta; solo lo puede hacer el autor de la receta
    @PostMapping
    public ResponseEntity<?> create(@RequestBody RecipeStep step) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(step.getRecipeId());

        if (recipeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Recipe recipe = recipeOptional.get();

        if (!recipe.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only modify steps of your own recipes.");
        }

        RecipeStep savedStep = recipeStepRepository.save(step);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStep);
    }

    // Actualiza un paso existente de una receta; solo lo puede editar el autor de la receta
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RecipeStep step) {
        Optional<RecipeStep> existingOptional = recipeStepRepository.findById(id);

        if (existingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RecipeStep existing = existingOptional.get();

        Optional<Recipe> recipeOptional = recipeRepository.findById(existing.getRecipeId());
        if (recipeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Recipe recipe = recipeOptional.get();

        if (!recipe.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only modify steps of your own recipes.");
        }

        step.setId(id);
        step.setRecipeId(existing.getRecipeId());

        RecipeStep updatedStep = recipeStepRepository.save(step);
        return ResponseEntity.ok(updatedStep);
    }

    // Elimina un paso de una receta; solo lo puede hacer el autor de la receta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<RecipeStep> existingOptional = recipeStepRepository.findById(id);

        if (existingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RecipeStep existing = existingOptional.get();

        Optional<Recipe> recipeOptional = recipeRepository.findById(existing.getRecipeId());
        if (recipeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Recipe recipe = recipeOptional.get();

        if (!recipe.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only modify steps of your own recipes.");
        }

        recipeStepRepository.deleteById(id);
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