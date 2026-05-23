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

    // Devuelve todos los retos disponibles en la aplicación
    @GetMapping
    public List<Challenge> getAll() {
        return repository.findAll();
    }

    // Devuelve un reto concreto por su identificador
    @GetMapping("/{id}")
    public Optional<Challenge> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // Crea un nuevo reto y lo persiste en la base de datos
    @PostMapping
    public Challenge create(@RequestBody Challenge challenge) {
        return repository.save(challenge);
    }

    // Actualiza los datos de un reto existente
    @PutMapping("/{id}")
    public Challenge update(@PathVariable Long id, @RequestBody Challenge challenge) {
        challenge.setId(id);
        return repository.save(challenge);
    }

    // Elimina un reto por su identificador
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
