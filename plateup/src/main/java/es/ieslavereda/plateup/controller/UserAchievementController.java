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

    @GetMapping
    public List<UserAchievement> getAll() {
        return repository.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<UserAchievement> getByUserId(@PathVariable Long userId) {
        return repository.findByUser_id(userId);
    }

    @GetMapping("/{userId}/{achievementId}")
    public Optional<UserAchievement> getById(@PathVariable Long userId, @PathVariable Long achievementId) {
        return repository.findById(new UserAchievementId(userId, achievementId));
    }

    @PostMapping
    public UserAchievement create(@RequestBody UserAchievement userAchievement) {
        return repository.save(userAchievement);
    }

    @PutMapping("/{userId}/{achievementId}")
    public UserAchievement update(
            @PathVariable Long userId,
            @PathVariable Long achievementId,
            @RequestBody UserAchievement userAchievement) {

        userAchievement.setUser_id(userId);
        userAchievement.setAchievement_id(achievementId);
        return repository.save(userAchievement);
    }

    @DeleteMapping("/{userId}/{achievementId}")
    public void delete(@PathVariable Long userId, @PathVariable Long achievementId) {
        repository.deleteById(new UserAchievementId(userId, achievementId));
    }
}