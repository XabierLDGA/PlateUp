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

    // Devuelve el progreso de todos los usuarios en todos los retos
    @GetMapping
    public List<UserChallenge> getAll() {
        return repository.findAll();
    }

    // Devuelve el progreso de un usuario en un reto concreto
    @GetMapping("/{userId}/{challengeId}")
    public Optional<UserChallenge> getById(@PathVariable Long userId, @PathVariable Long challengeId) {
        return repository.findById(new UserChallengeId(userId, challengeId));
    }

    // Asigna un reto a un usuario y guarda su estado inicial
    @PostMapping
    public UserChallenge create(@RequestBody UserChallenge userChallenge) {
        return repository.save(userChallenge);
    }

    // Actualiza el progreso o el estado de un reto para un usuario
    @PutMapping("/{userId}/{challengeId}")
    public UserChallenge update(
            @PathVariable Long userId,
            @PathVariable Long challengeId,
            @RequestBody UserChallenge userChallenge) {

        userChallenge.setUser_id(userId);
        userChallenge.setChallenge_id(challengeId);
        return repository.save(userChallenge);
    }

    // Elimina la relación de un usuario con un reto
    @DeleteMapping("/{userId}/{challengeId}")
    public void delete(@PathVariable Long userId, @PathVariable Long challengeId) {
        repository.deleteById(new UserChallengeId(userId, challengeId));
    }
}
