package es.ieslavereda.plateup.controller;

import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.Comment;
import es.ieslavereda.plateup.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentRepository repository;

    public CommentController(CommentRepository repository) {
        this.repository = repository;
    }

    // 🔹 GET todos los comentarios
    @GetMapping
    public List<Comment> getAll() {
        return repository.findAll();
    }

    // 🔹 GET comentario por ID
    @GetMapping("/{id}")
    public Optional<Comment> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // 🔹 GET comentarios por receta
    @GetMapping("/recipe/{recipeId}")
    public List<Comment> getByRecipeId(@PathVariable Long recipeId) {
        return repository.findByRecipeId(recipeId);
    }

    // 🔹 POST crear nuevo comentario
    @PostMapping
    public Comment create(@RequestBody Comment comment) {
        return repository.save(comment);
    }

    // 🔹 PUT actualizar comentario
    @PutMapping("/{id}")
    public Comment update(@PathVariable Long id, @RequestBody Comment comment) {
        comment.setId(id);
        return repository.save(comment);
    }

    // 🔹 DELETE eliminar comentario
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
