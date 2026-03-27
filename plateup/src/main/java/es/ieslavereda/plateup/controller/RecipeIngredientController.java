package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.model.RecipeIngredient;
import es.ieslavereda.plateup.repository.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipeingredients")
@CrossOrigin(origins = "*")
public class RecipeIngredientController {

    @Autowired
    private RecipeIngredientRepository repository;

    @GetMapping("/recipe/{recipeId}")
    public List<RecipeIngredient> getByRecipe(@PathVariable Long recipeId) {
        return repository.findByRecipeId(recipeId);
    }

    @PostMapping
    public RecipeIngredient create(@RequestBody RecipeIngredient recipeIngredient) {
        return repository.save(recipeIngredient);
    }

    @DeleteMapping("/{recipeId}/{ingredientId}")
    public void delete(@PathVariable Long recipeId,
                       @PathVariable Long ingredientId) {
        repository.deleteByRecipeIdAndIngredientId(recipeId, ingredientId);
    }
}