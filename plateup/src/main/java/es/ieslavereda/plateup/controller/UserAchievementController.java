package es.ieslavereda.plateup.controller;

import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.UserAchievement;
import es.ieslavereda.plateup.model.UserAchievementId;
import es.ieslavereda.plateup.repository.UserAchievementRepository;

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

    // 🔹 GET todos los logros de todos los usuarios
    @GetMapping
    public List<UserAchievement> getAll() {
        return repository.findAll();
    }

    // 🔹 GET logro específico de un usuario
    @GetMapping("/{userId}/{achievementId}")
    public Optional<UserAchievement> getById(@PathVariable Long userId, @PathVariable Long achievementId) {
        return repository.findById(new UserAchievementId(userId, achievementId));
    }

    // 🔹 POST asignar nuevo logro a usuario
    @PostMapping
    public UserAchievement create(@RequestBody UserAchievement userAchievement) {
        return repository.save(userAchievement);
    }

    // 🔹 PUT actualizar nivel o fecha de logro
    @PutMapping("/{userId}/{achievementId}")
    public UserAchievement update(
            @PathVariable Long userId,
            @PathVariable Long achievementId,
            @RequestBody UserAchievement userAchievement) {

        userAchievement.setUser_id(userId);
        userAchievement.setAchievement_id(achievementId);
        return repository.save(userAchievement);
    }

    // 🔹 DELETE eliminar un logro de un usuario
    @DeleteMapping("/{userId}/{achievementId}")
    public void delete(@PathVariable Long userId, @PathVariable Long achievementId) {
        repository.deleteById(new UserAchievementId(userId, achievementId));
    }
}
