package es.ieslavereda.plateup.controller;

import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.Challenge;
import es.ieslavereda.plateup.repository.ChallengeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/challenges")
@CrossOrigin(origins = "*")
public class ChallengeController {

    private final ChallengeRepository repository;

    public ChallengeController(ChallengeRepository repository) {
        this.repository = repository;
    }

    // 🔹 GET todos los challenges
    @GetMapping
    public List<Challenge> getAll() {
        return repository.findAll();
    }

    // 🔹 GET challenge por ID
    @GetMapping("/{id}")
    public Optional<Challenge> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // 🔹 POST crear un nuevo challenge
    @PostMapping
    public Challenge create(@RequestBody Challenge challenge) {
        return repository.save(challenge);
    }

    // 🔹 PUT actualizar un challenge existente
    @PutMapping("/{id}")
    public Challenge update(@PathVariable Long id, @RequestBody Challenge challenge) {
        challenge.setId(id);
        return repository.save(challenge);
    }

    // 🔹 DELETE eliminar challenge
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
