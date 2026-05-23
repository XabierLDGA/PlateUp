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

    // Devuelve todos los ingredientes disponibles en el catálogo
    @GetMapping
    public List<Ingredient> getAll() {
        return repository.findAll();
    }

    // Devuelve los ingredientes asociados a una receta concreta
    @GetMapping("/recipe/{recipeId}")
    public List<Ingredient> getByRecipeId(@PathVariable Long recipeId) {
        return repository.findByRecipeId(recipeId);
    }

    // Devuelve un ingrediente concreto por su identificador
    @GetMapping("/{id}")
    public Optional<Ingredient> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // Crea un nuevo ingrediente en el catálogo
    @PostMapping
    public Ingredient create(@RequestBody Ingredient ingredient) {
        return repository.save(ingredient);
    }

    // Actualiza los datos de un ingrediente existente
    @PutMapping("/{id}")
    public Ingredient update(@PathVariable Long id, @RequestBody Ingredient ingredient) {
        ingredient.setId(id);
        return repository.save(ingredient);
    }

    // Elimina un ingrediente del catálogo por su identificador
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
