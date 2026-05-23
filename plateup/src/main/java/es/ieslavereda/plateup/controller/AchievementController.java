package es.ieslavereda.plateup.controller;

import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.Achievement;
import es.ieslavereda.plateup.repository.AchievementRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*")
public class AchievementController {

    private final AchievementRepository repository;

    public AchievementController(AchievementRepository repository) {
        this.repository = repository;
    }

    // Devuelve todos los logros disponibles en la aplicación
    @GetMapping
    public List<Achievement> getAll() {
        return repository.findAll();
    }

    // Devuelve un logro concreto buscándolo por su identificador
    @GetMapping("/{id}")
    public Optional<Achievement> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // Crea un nuevo logro y lo guarda en la base de datos
    @PostMapping
    public Achievement create(@RequestBody Achievement achievement) {
        return repository.save(achievement);
    }

    // Actualiza los datos de un logro existente
    @PutMapping("/{id}")
    public Achievement update(@PathVariable Long id, @RequestBody Achievement achievement) {
        achievement.setId(id);
        return repository.save(achievement);
    }

    // Elimina un logro por su identificador
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
