package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.exception.AuthenticationRequiredException;
import es.ieslavereda.plateup.model.Collection;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.CollectionRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/collections")
@CrossOrigin(origins = "*")
public class CollectionController {

    private final CollectionRepository repository;
    private final UserRepository userRepository;

    public CollectionController(CollectionRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // Devuelve todas las colecciones existentes
    @GetMapping
    public List<Collection> getAll() {
        return repository.findAll();
    }

    // Devuelve una colección concreta por su identificador
    @GetMapping("/{id}")
    public Optional<Collection> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // Devuelve todas las colecciones que pertenecen a un usuario
    @GetMapping("/user/{userId}")
    public List<Collection> getByUserId(@PathVariable Long userId) {
        return repository.findByUserId(userId);
    }

    // Crea una nueva colección asociándola al usuario autenticado
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Collection collection) {
        User authenticatedUser = getAuthenticatedUser();

        collection.setUserId(authenticatedUser.getId());

        if (collection.getCreatedAt() == null) {
            collection.setCreatedAt(LocalDateTime.now());
        }

        Collection savedCollection = repository.save(collection);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCollection);
    }

    // Actualiza una colección existente; solo la puede modificar su propietario
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Collection collection) {
        Optional<Collection> existingOptional = repository.findById(id);

        if (existingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Collection existing = existingOptional.get();

        if (!existing.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only update your own collections.");
        }

        collection.setId(existing.getId());
        collection.setUserId(existing.getUserId());

        if (collection.getCreatedAt() == null) {
            collection.setCreatedAt(existing.getCreatedAt());
        }

        Collection updatedCollection = repository.save(collection);
        return ResponseEntity.ok(updatedCollection);
    }

    // Elimina una colección; solo la puede borrar su propietario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Collection> existingOptional = repository.findById(id);

        if (existingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        Collection existing = existingOptional.get();

        if (!existing.getUserId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only delete your own collections.");
        }

        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Obtiene el usuario autenticado a partir del contexto de seguridad de Spring
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationRequiredException("Authenticated user not found");
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AuthenticationRequiredException("Authenticated user not found"));
    }
}