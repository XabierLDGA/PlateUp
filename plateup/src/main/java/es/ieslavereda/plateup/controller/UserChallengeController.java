package es.ieslavereda.plateup.controller;

import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.UserChallenge;
import es.ieslavereda.plateup.model.UserChallengeId;
import es.ieslavereda.plateup.repository.UserChallengeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/userchallenges")
@CrossOrigin(origins = "*")
public class UserChallengeController {

    private final UserChallengeRepository repository;

    public UserChallengeController(UserChallengeRepository repository) {
        this.repository = repository;
    }

    // 🔹 GET todos los retos de todos los usuarios
    @GetMapping
    public List<UserChallenge> getAll() {
        return repository.findAll();
    }

    // 🔹 GET reto específico de un usuario
    @GetMapping("/{userId}/{challengeId}")
    public Optional<UserChallenge> getById(@PathVariable Long userId, @PathVariable Long challengeId) {
        return repository.findById(new UserChallengeId(userId, challengeId));
    }

    // 🔹 POST asignar un reto a un usuario
    @PostMapping
    public UserChallenge create(@RequestBody UserChallenge userChallenge) {
        return repository.save(userChallenge);
    }

    // 🔹 PUT actualizar progreso o estado de un reto
    @PutMapping("/{userId}/{challengeId}")
    public UserChallenge update(
            @PathVariable Long userId,
            @PathVariable Long challengeId,
            @RequestBody UserChallenge userChallenge) {

        userChallenge.setUser_id(userId);
        userChallenge.setChallenge_id(challengeId);
        return repository.save(userChallenge);
    }

    // 🔹 DELETE eliminar un reto de un usuario
    @DeleteMapping("/{userId}/{challengeId}")
    public void delete(@PathVariable Long userId, @PathVariable Long challengeId) {
        repository.deleteById(new UserChallengeId(userId, challengeId));
    }
}
