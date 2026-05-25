package es.ieslavereda.plateup.config;

import es.ieslavereda.plateup.service.AchievementUnlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AchievementSyncRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AchievementSyncRunner.class);

    private final AchievementUnlockService achievementUnlockService;

    public AchievementSyncRunner(AchievementUnlockService achievementUnlockService) {
        this.achievementUnlockService = achievementUnlockService;
    }

    // Lanza la sincronización de logros al inicio; si falla, registra el error pero no impide que la app arranque
    @Override
    public void run(String... args) {
        try {
            achievementUnlockService.syncAllUsersAchievements();
        } catch (Exception e) {
            log.error("Achievement sync failed on startup: {}", e.getMessage(), e);
        }
    }
}