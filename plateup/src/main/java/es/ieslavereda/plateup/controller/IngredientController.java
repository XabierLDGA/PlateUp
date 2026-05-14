package es.ieslavereda.plateup.controller;

import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.Ingredient;
import es.ieslavereda.plateup.repository.IngredientRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "*")
public class IngredientController {

    private final IngredientRepository repository;

    public IngredientController(IngredientRepository repository) {
        this.repository = repository;
    }

    // 🔹 GET todos los ingredientes
    @GetMapping
    public List<Ingredient> getAll() {
        return repository.findAll();
    }

    // 🔹 GET ingredientes de una receta específica
    @GetMapping("/recipe/{recipeId}")
    public List<Ingredient> getByRecipeId(@PathVariable Long recipeId) {
        return repository.findByRecipeId(recipeId);
    }

    // 🔹 GET ingrediente por ID
    @GetMapping("/{id}")
    public Optional<Ingredient> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // 🔹 POST crear nuevo ingrediente
    @PostMapping
    public Ingredient create(@RequestBody Ingredient ingredient) {
        return repository.save(ingredient);
    }

    // 🔹 PUT actualizar ingrediente
    @PutMapping("/{id}")
    public Ingredient update(@PathVariable Long id, @RequestBody Ingredient ingredient) {
        ingredient.setId(id);
        return repository.save(ingredient);
    }

    // 🔹 DELETE eliminar ingrediente
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
