package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.model.RecipeStep;
import es.ieslavereda.plateup.repository.RecipeStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipesteps")
@CrossOrigin(origins = "*")
public class RecipeStepController {

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @GetMapping("/recipe/{recipeId}")
    public List<RecipeStep> getStepsByRecipe(@PathVariable Long recipeId) {
        return recipeStepRepository.findByRecipeId(recipeId);
    }

    @PostMapping
    public RecipeStep create(@RequestBody RecipeStep step) {
        return recipeStepRepository.save(step);
    }

    @PutMapping("/{id}")
    public RecipeStep update(@PathVariable Long id, @RequestBody RecipeStep step) {
        step.setId(id);
        return recipeStepRepository.save(step);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        recipeStepRepository.deleteById(id);
    }
}