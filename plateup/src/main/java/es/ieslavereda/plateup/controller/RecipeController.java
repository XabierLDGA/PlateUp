package es.ieslavereda.plateup.controller;

import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.Recipe;
import es.ieslavereda.plateup.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeRepository repository;

    public RecipeController(RecipeRepository repository) {
        this.repository = repository;
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
        return repository.save(recipe);
    }

    @PutMapping("/{id}")
    public Recipe update(@PathVariable Long id, @RequestBody Recipe recipe) {
        Recipe existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        recipe.setId(existing.getId());
        return repository.save(recipe);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}