package es.ieslavereda.plateup.controller;

import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.Like;
import es.ieslavereda.plateup.model.LikeId;
import es.ieslavereda.plateup.repository.LikeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
public class LikeController {

    private final LikeRepository repository;

    public LikeController(LikeRepository repository) {
        this.repository = repository;
    }

    // 🔹 GET todos los likes
    @GetMapping
    public List<Like> getAll() {
        return repository.findAll();
    }

    // 🔹 GET un like concreto (por user_id y recipe_id)
    @GetMapping("/{userId}/{recipeId}")
    public Optional<Like> getById(@PathVariable Long userId, @PathVariable Long recipeId) {
        return repository.findById(new LikeId(userId, recipeId));
    }

    // 🔹 POST crear un nuevo like
    @PostMapping
    public Like create(@RequestBody Like like) {
        return repository.save(like);
    }

    // 🔹 DELETE eliminar un like (un usuario quita su “me gusta”)
    @DeleteMapping("/{userId}/{recipeId}")
    public void delete(@PathVariable Long userId, @PathVariable Long recipeId) {
        repository.deleteById(new LikeId(userId, recipeId));
    }
}
