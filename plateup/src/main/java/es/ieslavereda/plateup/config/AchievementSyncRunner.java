package es.ieslavereda.plateup.config;

import es.ieslavereda.plateup.service.AchievementUnlockService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AchievementSyncRunner implements CommandLineRunner {

    private final AchievementUnlockService achievementUnlockService;

    public AchievementSyncRunner(AchievementUnlockService achievementUnlockService) {
        this.achievementUnlockService = achievementUnlockService;
    }

    @Override
    public void run(String... args) {
        achievementUnlockService.syncAllUsersAchievements();
    }
}