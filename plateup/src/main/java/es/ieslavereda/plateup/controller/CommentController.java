package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.exception.AuthenticationRequiredException;
import es.ieslavereda.plateup.model.Comment;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.CommentRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentRepository repository;
    private final UserRepository userRepository;

    public CommentController(CommentRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Comment> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Comment> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    @GetMapping("/recipe/{recipeId}")
    public List<Comment> getByRecipeId(@PathVariable Long recipeId) {
        return repository.findByRecipeId(recipeId);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Comment comment) {
        User authenticatedUser = getAuthenticatedUser();

        comment.setUserId(authenticatedUser.getId());

        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        }

        Comment savedComment = repository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Comment comment) {
        Optional<Comment> existingOptional = repository.findById(id);

        if (existingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Comment existing = existingOptional.get();

        if (!existing.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only update your own comments.");
        }

        comment.setId(existing.getId());
        comment.setUserId(existing.getUserId());

        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(existing.getCreatedAt());
        }

        Comment updatedComment = repository.save(comment);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Comment> existingOptional = repository.findById(id);

        if (existingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Comment existing = existingOptional.get();

        if (!existing.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only delete your own comments.");
        }

        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationRequiredException("Authenticated user not found");
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AuthenticationRequiredException("Authenticated user not found"));
    }
}