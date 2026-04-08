package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.model.Recipe;
import es.ieslavereda.plateup.model.RecipeIngredient;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.RecipeIngredientRepository;
import es.ieslavereda.plateup.repository.RecipeRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipeingredients")
@CrossOrigin(origins = "*")
public class RecipeIngredientController {

    private final RecipeIngredientRepository repository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeIngredientController(
            RecipeIngredientRepository repository,
            RecipeRepository recipeRepository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/recipe/{recipeId}")
    public List<RecipeIngredient> getByRecipe(@PathVariable Long recipeId) {
        return repository.findByRecipeId(recipeId);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RecipeIngredient recipeIngredient) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeIngredient.getRecipeId());

        if (recipeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Recipe recipe = recipeOptional.get();

        if (!recipe.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only modify ingredients of your own recipes.");
        }

        RecipeIngredient saved = repository.save(recipeIngredient);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{recipeId}/{ingredientId}")
    public ResponseEntity<?> delete(@PathVariable Long recipeId,
                                    @PathVariable Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Recipe recipe = recipeOptional.get();

        if (!recipe.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only modify ingredients of your own recipes.");
        }

        repository.deleteByRecipeIdAndIngredientId(recipeId, ingredientId);
        return ResponseEntity.ok().build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Authenticated user not found");
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}