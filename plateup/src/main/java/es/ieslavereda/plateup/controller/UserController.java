package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.dto.LoginRequest;
import es.ieslavereda.plateup.dto.RegisterRequest;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<User> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return repository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String identifier = request.getIdentifier() == null ? "" : request.getIdentifier().trim();
        String password = request.getPassword() == null ? "" : request.getPassword().trim();

        if (identifier.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(error("Username/email and password are required."));
        }

        Optional<User> userOptional = identifier.contains("@")
                ? repository.findByEmail(identifier)
                : repository.findByUsername(identifier);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(error("The username/email or password is incorrect."));
        }

        User user = userOptional.get();

        if (user.getPasswordHash() == null || !user.getPasswordHash().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(error("The username/email or password is incorrect."));
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String username = request.getUsername() == null ? "" : request.getUsername().trim();
        String email = request.getEmail() == null ? "" : request.getEmail().trim();
        String password = request.getPassword() == null ? "" : request.getPassword().trim();
        String displayName = request.getDisplayName() == null ? "" : request.getDisplayName().trim();
        String bio = request.getBio() == null ? "" : request.getBio().trim();
        String avatarUrl = request.getAvatarUrl() == null ? "" : request.getAvatarUrl().trim();
        String visibilityDefault = request.getVisibilityDefault() == null
                ? "public"
                : request.getVisibilityDefault().trim().toLowerCase();

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(error("Username, email and password are required."));
        }

        if (displayName.isBlank()) {
            displayName = username;
        }

        if (!(visibilityDefault.equals("private") || visibilityDefault.equals("followers") || visibilityDefault.equals("public"))) {
            visibilityDefault = "public";
        }

        if (repository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error("That username is already in use."));
        }

        if (repository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error("That email is already in use."));
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setDisplayName(displayName);
        user.setBio(bio);
        user.setAvatarUrl(avatarUrl);
        user.setVisibilityDefault(visibilityDefault);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User createdUser = repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return repository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return body;
    }
}