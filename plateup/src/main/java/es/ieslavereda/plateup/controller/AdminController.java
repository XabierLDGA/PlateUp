package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.exception.AuthenticationRequiredException;
import es.ieslavereda.plateup.model.Recipe;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.CookedRecipeRepository;
import es.ieslavereda.plateup.repository.LikeRepository;
import es.ieslavereda.plateup.repository.RecipeRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    // Repositorios necesarios para consultar las distintas entidades del panel de administración
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final LikeRepository likeRepository;
    private final CookedRecipeRepository cookedRecipeRepository;

    public AdminController(
            UserRepository userRepository,
            RecipeRepository recipeRepository,
            LikeRepository likeRepository,
            CookedRecipeRepository cookedRecipeRepository
    ) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.likeRepository = likeRepository;
        this.cookedRecipeRepository = cookedRecipeRepository;
    }

    // Devuelve un resumen con los totales de usuarios, recetas, likes y recetas cocinadas
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        checkAdminAccess();
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalRecipes", recipeRepository.count());
        stats.put("totalLikes", likeRepository.count());
        stats.put("totalCooked", cookedRecipeRepository.count());
        return ResponseEntity.ok(stats);
    }

    // Devuelve cuántas recetas hay por cada categoría, útil para las gráficas del panel
    @GetMapping("/recipes-by-category")
    public ResponseEntity<?> getRecipesByCategory() {
        checkAdminAccess();
        Map<String, Long> result = new LinkedHashMap<>();
        recipeRepository.countByCategory().forEach(row ->
                result.put((String) row[0], (Long) row[1])
        );
        return ResponseEntity.ok(result);
    }

    // Devuelve los 5 usuarios con más recetas publicadas
    @GetMapping("/top-users")
    public ResponseEntity<?> getTopUsers() {
        checkAdminAccess();
        List<Object[]> rows = recipeRepository.countByUserGrouped(PageRequest.of(0, 5));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Long userId = (Long) row[0];
            Long count = (Long) row[1];
            userRepository.findById(userId).ifPresent(user -> {
                Map<String, Object> entry = new HashMap<>();
                entry.put("userId", userId);
                entry.put("username", user.getUsername());
                entry.put("displayName", user.getDisplayName());
                entry.put("avatarUrl", user.getAvatarUrl());
                entry.put("recipeCount", count);
                result.add(entry);
            });
        }
        return ResponseEntity.ok(result);
    }

    // Devuelve las 5 recetas con más likes
    @GetMapping("/top-recipes")
    public ResponseEntity<?> getTopRecipes() {
        checkAdminAccess();
        List<Object[]> rows = likeRepository.topRecipesByLikes(PageRequest.of(0, 5));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Long recipeId = (Long) row[0];
            Long likeCount = (Long) row[1];
            recipeRepository.findById(recipeId).ifPresent(recipe -> {
                Map<String, Object> entry = new HashMap<>();
                entry.put("recipeId", recipeId);
                entry.put("title", recipe.getTitle());
                entry.put("imageUrl", recipe.getImageUrl());
                entry.put("category", recipe.getCategory());
                entry.put("likeCount", likeCount);
                result.add(entry);
            });
        }
        return ResponseEntity.ok(result);
    }

    // Devuelve el número de usuarios nuevos registrados en los últimos 6 meses, agrupados por mes
    @GetMapping("/users-by-month")
    public ResponseEntity<?> getUsersByMonth() {
        checkAdminAccess();
        LocalDateTime since = LocalDateTime.now().minusMonths(6);
        Map<String, Long> result = new LinkedHashMap<>();
        userRepository.countByMonth(since).forEach(row ->
                result.put((String) row[0], ((Number) row[1]).longValue())
        );
        return ResponseEntity.ok(result);
    }

    // Lanza una excepción si el usuario autenticado no tiene el rol de administrador
    private void checkAdminAccess() {
        User user = getAuthenticatedUser();
        if (!"ADMIN".equals(user.getRole())) {
            throw new SecurityException("Admin access required");
        }
    }

    // Obtiene el usuario autenticado a partir del contexto de seguridad de Spring
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationRequiredException("Authenticated user not found");
        }
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AuthenticationRequiredException("Authenticated user not found"));
    }
}
