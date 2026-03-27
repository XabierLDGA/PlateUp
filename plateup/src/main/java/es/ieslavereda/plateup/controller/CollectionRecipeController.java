package es.ieslavereda.plateup.controller;

import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.CollectionRecipe;
import es.ieslavereda.plateup.model.CollectionRecipeId;
import es.ieslavereda.plateup.repository.CollectionRecipeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collectionrecipes")
@CrossOrigin(origins = "*")
public class CollectionRecipeController {

    private final CollectionRecipeRepository repository;

    public CollectionRecipeController(CollectionRecipeRepository repository) {
        this.repository = repository;
    }

    // 🔹 GET todos los registros
    @GetMapping
    public List<CollectionRecipe> getAll() {
        return repository.findAll();
    }

    // 🔹 GET por ID compuesto (collection_id + recipe_id)
    @GetMapping("/{collectionId}/{recipeId}")
    public Optional<CollectionRecipe> getById(@PathVariable Long collectionId, @PathVariable Long recipeId) {
        return repository.findById(new CollectionRecipeId(collectionId, recipeId));
    }

    // 🔹 POST crear una nueva relación colección-receta
    @PostMapping
    public CollectionRecipe create(@RequestBody CollectionRecipe collectionRecipe) {
        return repository.save(collectionRecipe);
    }

    // 🔹 PUT actualizar el orden de una receta dentro de una colección
    @PutMapping("/{collectionId}/{recipeId}")
    public CollectionRecipe update(@PathVariable Long collectionId, @PathVariable Long recipeId, @RequestBody CollectionRecipe collectionRecipe) {
        collectionRecipe.setCollection_id(collectionId);
        collectionRecipe.setRecipe_id(recipeId);
        return repository.save(collectionRecipe);
    }

    // 🔹 DELETE eliminar una receta de una colección
    @DeleteMapping("/{collectionId}/{recipeId}")
    public void delete(@PathVariable Long collectionId, @PathVariable Long recipeId) {
        repository.deleteById(new CollectionRecipeId(collectionId, recipeId));
    }
}
