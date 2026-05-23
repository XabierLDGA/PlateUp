package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.model.UserAchievement;
import es.ieslavereda.plateup.model.UserAchievementId;
import es.ieslavereda.plateup.repository.UserAchievementRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/userachievements")
@CrossOrigin(origins = "*")
public class UserAchievementController {

    private final UserAchievementRepository repository;

    public UserAchievementController(UserAchievementRepository repository) {
        this.repository = repository;
    }

    // Devuelve todos los logros desbloqueados por todos los usuarios
    @GetMapping
    public List<UserAchievement> getAll() {
        return repository.findAll();
    }

    // Devuelve los logros que ha desbloqueado un usuario concreto
    @GetMapping("/user/{userId}")
    public List<UserAchievement> getByUserId(@PathVariable Long userId) {
        return repository.findByUser_id(userId);
    }

    // Comprueba si un usuario tiene un logro concreto desbloqueado
    @GetMapping("/{userId}/{achievementId}")
    public Optional<UserAchievement> getById(@PathVariable Long userId, @PathVariable Long achievementId) {
        return repository.findById(new UserAchievementId(userId, achievementId));
    }

    // Registra manualmente un logro desbloqueado para un usuario
    @PostMapping
    public UserAchievement create(@RequestBody UserAchievement userAchievement) {
        return repository.save(userAchievement);
    }

    // Actualiza los datos de un logro desbloqueado por un usuario
    @PutMapping("/{userId}/{achievementId}")
    public UserAchievement update(
            @PathVariable Long userId,
            @PathVariable Long achievementId,
            @RequestBody UserAchievement userAchievement) {

        userAchievement.setUser_id(userId);
        userAchievement.setAchievement_id(achievementId);
        return repository.save(userAchievement);
    }

    // Elimina un logro desbloqueado de un usuario
    @DeleteMapping("/{userId}/{achievementId}")
    public void delete(@PathVariable Long userId, @PathVariable Long achievementId) {
        repository.deleteById(new UserAchievementId(userId, achievementId));
    }
}