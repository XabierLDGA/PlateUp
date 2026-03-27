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

    // 🔹 GET todos los achievements
    @GetMapping
    public List<Achievement> getAll() {
        return repository.findAll();
    }

    // 🔹 GET achievement por ID
    @GetMapping("/{id}")
    public Optional<Achievement> getById(@PathVariable Long id) {
        return repository.findById(id);
    }

    // 🔹 POST crear nuevo achievement
    @PostMapping
    public Achievement create(@RequestBody Achievement achievement) {
        return repository.save(achievement);
    }

    // 🔹 PUT actualizar achievement
    @PutMapping("/{id}")
    public Achievement update(@PathVariable Long id, @RequestBody Achievement achievement) {
        achievement.setId(id);
        return repository.save(achievement);
    }

    // 🔹 DELETE eliminar achievement
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
