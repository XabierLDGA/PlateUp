package es.ieslavereda.plateup.service;

import es.ieslavereda.plateup.model.UserAchievement;
import es.ieslavereda.plateup.repository.CookedRecipeRepository;
import es.ieslavereda.plateup.repository.FollowRepository;
import es.ieslavereda.plateup.repository.LikeRepository;
import es.ieslavereda.plateup.repository.RecipeRepository;
import es.ieslavereda.plateup.repository.UserAchievementRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AchievementUnlockService {

    // Cada mapa relaciona el ID del logro con el número mínimo de acciones que necesita el usuario para desbloquearlo
    private static final Map<Long, Integer> RECIPE_ACHIEVEMENTS = new LinkedHashMap<>();
    private static final Map<Long, Integer> LIKE_ACHIEVEMENTS = new LinkedHashMap<>();
    private static final Map<Long, Integer> COOKED_ACHIEVEMENTS = new LinkedHashMap<>();
    private static final Map<Long, Integer> FOLLOWER_ACHIEVEMENTS = new LinkedHashMap<>();

    static {
        // Logros por recetas publicadas: 1, 10 y 25 recetas
        RECIPE_ACHIEVEMENTS.put(1L, 1);
        RECIPE_ACHIEVEMENTS.put(2L, 10);
        RECIPE_ACHIEVEMENTS.put(3L, 25);

        // Logros por likes recibidos: 1, 10 y 25 likes
        LIKE_ACHIEVEMENTS.put(4L, 1);
        LIKE_ACHIEVEMENTS.put(5L, 10);
        LIKE_ACHIEVEMENTS.put(6L, 25);

        // Logros por recetas cocinadas: 1, 10 y 25 recetas
        COOKED_ACHIEVEMENTS.put(7L, 1);
        COOKED_ACHIEVEMENTS.put(8L, 10);
        COOKED_ACHIEVEMENTS.put(9L, 25);

        // Logros por seguidores: 1, 10 y 25 seguidores
        FOLLOWER_ACHIEVEMENTS.put(10L, 1);
        FOLLOWER_ACHIEVEMENTS.put(11L, 10);
        FOLLOWER_ACHIEVEMENTS.put(12L, 25);
    }

    private final RecipeRepository recipeRepository;
    private final LikeRepository likeRepository;
    private final CookedRecipeRepository cookedRecipeRepository;
    private final FollowRepository followRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserRepository userRepository;

    public AchievementUnlockService(
            RecipeRepository recipeRepository,
            LikeRepository likeRepository,
            CookedRecipeRepository cookedRecipeRepository,
            FollowRepository followRepository,
            UserAchievementRepository userAchievementRepository,
            UserRepository userRepository
    ) {
        this.recipeRepository = recipeRepository;
        this.likeRepository = likeRepository;
        this.cookedRecipeRepository = cookedRecipeRepository;
        this.followRepository = followRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.userRepository = userRepository;
    }

    // Comprueba si el usuario ha desbloqueado algún logro relacionado con las recetas que ha publicado
    @Transactional
    public void checkRecipeAchievements(Long userId) {
        if (!userExists(userId)) {
            return;
        }

        long totalRecipes = recipeRepository.countByUserId(userId);
        unlockAchievementsByThreshold(userId, totalRecipes, RECIPE_ACHIEVEMENTS);
    }

    // Comprueba los logros basados en los likes que han recibido las recetas del usuario
    @Transactional
    public void checkLikeAchievements(Long userId) {
        if (!userExists(userId)) {
            return;
        }

        long totalLikesReceived = likeRepository.countLikesReceivedByUserId(userId);
        unlockAchievementsByThreshold(userId, totalLikesReceived, LIKE_ACHIEVEMENTS);
    }

    // Comprueba los logros por recetas que el usuario ha marcado como cocinadas
    @Transactional
    public void checkCookedAchievements(Long userId) {
        if (!userExists(userId)) {
            return;
        }

        long totalCookedRecipes = cookedRecipeRepository.countByUserId(userId);
        unlockAchievementsByThreshold(userId, totalCookedRecipes, COOKED_ACHIEVEMENTS);
    }

    // Comprueba los logros según el número de seguidores aceptados que tiene el usuario
    @Transactional
    public void checkFollowerAchievements(Long userId) {
        if (!userExists(userId)) {
            return;
        }

        // Solo contamos los seguidores cuya solicitud ha sido aceptada
        long totalFollowers = followRepository.countByFollowedIdAndStatus(userId, "accepted");
        unlockAchievementsByThreshold(userId, totalFollowers, FOLLOWER_ACHIEVEMENTS);
    }

    // Lanza todas las comprobaciones de logros para un usuario de una sola vez
    @Transactional
    public void checkAllAchievementsForUser(Long userId) {
        if (!userExists(userId)) {
            return;
        }

        checkRecipeAchievements(userId);
        checkLikeAchievements(userId);
        checkCookedAchievements(userId);
        checkFollowerAchievements(userId);
    }

    // Recorre todos los usuarios de la app y sincroniza sus logros (útil al arrancar o para tareas de mantenimiento)
    @Transactional
    public void syncAllUsersAchievements() {
        List<Long> userIds = userRepository.findAll()
                .stream()
                .map(user -> user.getId())
                .toList();

        for (Long userId : userIds) {
            checkAllAchievementsForUser(userId);
        }
    }

    // Recorre los umbrales del mapa y desbloquea los logros que el usuario ya ha superado
    private void unlockAchievementsByThreshold(Long userId, long currentValue, Map<Long, Integer> achievementThresholds) {
        for (Map.Entry<Long, Integer> entry : achievementThresholds.entrySet()) {
            Long achievementId = entry.getKey();
            Integer requiredValue = entry.getValue();

            if (currentValue >= requiredValue) {
                unlockAchievement(userId, achievementId);
            }
        }
    }

    // Guarda el logro para el usuario si todavía no lo tiene
    private void unlockAchievement(Long userId, Long achievementId) {
        // Si ya lo tiene, no hacemos nada para evitar duplicados
        if (userAchievementRepository.existsByUser_idAndAchievement_id(userId, achievementId)) {
            return;
        }

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUser_id(userId);
        userAchievement.setAchievement_id(achievementId);
        userAchievement.setLevel(1);
        userAchievement.setEarned_at(LocalDateTime.now());

        userAchievementRepository.save(userAchievement);
    }

    // Devuelve true si el usuario existe en la base de datos, para no operar con IDs inválidos
    private boolean userExists(Long userId) {
        return userId != null && userRepository.existsById(userId);
    }
}
