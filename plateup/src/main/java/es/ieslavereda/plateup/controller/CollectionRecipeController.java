package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.model.Collection;
import es.ieslavereda.plateup.model.CollectionRecipe;
import es.ieslavereda.plateup.model.CollectionRecipeId;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.CollectionRecipeRepository;
import es.ieslavereda.plateup.repository.CollectionRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collectionrecipes")
@CrossOrigin(origins = "*")
public class CollectionRecipeController {

    private final CollectionRecipeRepository repository;
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;

    public CollectionRecipeController(
            CollectionRecipeRepository repository,
            CollectionRepository collectionRepository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.collectionRepository = collectionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<CollectionRecipe> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{collectionId}/{recipeId}")
    public Optional<CollectionRecipe> getById(@PathVariable Long collectionId, @PathVariable Long recipeId) {
        return repository.findById(new CollectionRecipeId(collectionId, recipeId));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CollectionRecipe collectionRecipe) {
        Optional<Collection> collectionOptional = collectionRepository.findById(collectionRecipe.getCollection_id());

        if (collectionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Collection collection = collectionOptional.get();

        if (!collection.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only modify your own collections.");
        }

        CollectionRecipe saved = repository.save(collectionRecipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{collectionId}/{recipeId}")
    public ResponseEntity<?> update(
            @PathVariable Long collectionId,
            @PathVariable Long recipeId,
            @RequestBody CollectionRecipe collectionRecipe
    ) {
        Optional<CollectionRecipe> existingOptional = repository.findById(new CollectionRecipeId(collectionId, recipeId));

        if (existingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Collection> collectionOptional = collectionRepository.findById(collectionId);
        if (collectionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Collection collection = collectionOptional.get();

        if (!collection.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only modify your own collections.");
        }

        collectionRecipe.setCollection_id(collectionId);
        collectionRecipe.setRecipe_id(recipeId);

        CollectionRecipe updated = repository.save(collectionRecipe);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{collectionId}/{recipeId}")
    public ResponseEntity<?> delete(@PathVariable Long collectionId, @PathVariable Long recipeId) {
        Optional<CollectionRecipe> existingOptional = repository.findById(new CollectionRecipeId(collectionId, recipeId));

        if (existingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Collection> collectionOptional = collectionRepository.findById(collectionId);
        if (collectionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Collection collection = collectionOptional.get();

        if (!collection.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only modify your own collections.");
        }

        repository.deleteById(new CollectionRecipeId(collectionId, recipeId));
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