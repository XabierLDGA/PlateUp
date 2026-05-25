package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.exception.AuthenticationRequiredException;
import es.ieslavereda.plateup.dto.AuthResponse;
import es.ieslavereda.plateup.dto.LoginRequest;
import es.ieslavereda.plateup.dto.RegisterRequest;
import es.ieslavereda.plateup.model.Collection;
import es.ieslavereda.plateup.model.Recipe;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.CollectionRecipeRepository;
import es.ieslavereda.plateup.repository.CollectionRepository;
import es.ieslavereda.plateup.repository.CommentRepository;
import es.ieslavereda.plateup.repository.CookedRecipeRepository;
import es.ieslavereda.plateup.repository.FollowRepository;
import es.ieslavereda.plateup.repository.LikeRepository;
import es.ieslavereda.plateup.repository.RecipeIngredientRepository;
import es.ieslavereda.plateup.repository.RecipeRepository;
import es.ieslavereda.plateup.repository.RecipeStepRepository;
import es.ieslavereda.plateup.repository.RecipeUtensilRepository;
import es.ieslavereda.plateup.repository.UserAchievementRepository;
import es.ieslavereda.plateup.repository.UserChallengeRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import es.ieslavereda.plateup.security.JwtService;
import es.ieslavereda.plateup.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    // Muchos repositorios necesarios para el borrado en cascada al eliminar un usuario
    private final UserRepository repository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RecipeUtensilRepository recipeUtensilRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final CookedRecipeRepository cookedRecipeRepository;
    private final FollowRepository followRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final CollectionRepository collectionRepository;
    private final CollectionRecipeRepository collectionRecipeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final FileStorageService fileStorageService;

    public UserController(
            UserRepository repository,
            RecipeRepository recipeRepository,
            RecipeIngredientRepository recipeIngredientRepository,
            RecipeStepRepository recipeStepRepository,
            RecipeUtensilRepository recipeUtensilRepository,
            LikeRepository likeRepository,
            CommentRepository commentRepository,
            CookedRecipeRepository cookedRecipeRepository,
            FollowRepository followRepository,
            UserAchievementRepository userAchievementRepository,
            UserChallengeRepository userChallengeRepository,
            CollectionRepository collectionRepository,
            CollectionRecipeRepository collectionRecipeRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            FileStorageService fileStorageService
    ) {
        this.repository = repository;
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeStepRepository = recipeStepRepository;
        this.recipeUtensilRepository = recipeUtensilRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.cookedRecipeRepository = cookedRecipeRepository;
        this.followRepository = followRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.collectionRepository = collectionRepository;
        this.collectionRecipeRepository = collectionRecipeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.fileStorageService = fileStorageService;
    }

    // Devuelve todos los usuarios registrados en la aplicación
    @GetMapping
    public List<User> getAll() {
        return repository.findAll();
    }

    // Devuelve un usuario concreto por su identificador
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crea un usuario directamente; si la contraseña no está encriptada, la encripta antes de guardar
    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank() && !isBcryptHash(user.getPasswordHash())) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }

        if (user.getStreakCount() == null) {
            user.setStreakCount(0);
        }

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        user.setUpdatedAt(LocalDateTime.now());
        return repository.save(user);
    }

    // Autentica a un usuario con su nombre de usuario o email y devuelve un token JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String identifier = request.getIdentifier() == null ? "" : request.getIdentifier().trim();
        String password = request.getPassword() == null ? "" : request.getPassword().trim();

        if (identifier.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(error("Username/email and password are required."));
        }

        // Si el identificador contiene @ lo tratamos como email; si no, como nombre de usuario
        Optional<User> userOptional = identifier.contains("@")
                ? repository.findByEmail(identifier)
                : repository.findByUsername(identifier);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(error("The username/email or password is incorrect."));
        }

        User user = userOptional.get();

        if (!matchesPasswordAndMigrateLegacy(user, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(error("The username/email or password is incorrect."));
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user));
    }

    // Registra un nuevo usuario, valida los datos y devuelve un token JWT al terminar
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String username = request.getUsername() == null ? "" : request.getUsername().trim();
        String email = request.getEmail() == null ? "" : request.getEmail().trim();
        String password = request.getPassword() == null ? "" : request.getPassword().trim();
        String displayName = request.getDisplayName() == null ? "" : request.getDisplayName().trim();
        String bio = request.getBio() == null ? "" : request.getBio().trim();
        String avatarUrl = request.getAvatarUrl() == null ? "" : request.getAvatarUrl().trim();
        String visibilityDefault = request.getVisibilityDefault() == null
                ? "public"
                : request.getVisibilityDefault().trim().toLowerCase();

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(error("Username, email and password are required."));
        }

        if (displayName.isBlank()) {
            displayName = username;
        }

        // Si el valor de visibilidad no es uno de los permitidos, usamos "public" como valor por defecto
        if (!(visibilityDefault.equals("private") || visibilityDefault.equals("followers") || visibilityDefault.equals("public"))) {
            visibilityDefault = "public";
        }

        if (repository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error("That username is already in use."));
        }

        if (repository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error("That email is already in use."));
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setDisplayName(displayName);
        user.setBio(bio);
        user.setAvatarUrl(avatarUrl);
        user.setVisibilityDefault(visibilityDefault);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setStreakCount(0);
        user.setLastActiveDate(null);

        User createdUser = repository.save(user);
        String token = jwtService.generateToken(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token, createdUser));
    }

    // Actualiza el perfil de un usuario; solo lo puede hacer el propio usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User user) {
        Optional<User> existingOptional = repository.findById(id);

        if (existingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        if (!authenticatedUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(error("You can only update your own account."));
        }

        User existing = existingOptional.get();
        String previousAvatarUrl = existing.getAvatarUrl();

        existing.setUsername(valueOrDefault(user.getUsername(), existing.getUsername()));
        existing.setEmail(valueOrDefault(user.getEmail(), existing.getEmail()));
        existing.setDisplayName(valueOrDefault(user.getDisplayName(), existing.getDisplayName()));
        existing.setBio(valueOrDefault(user.getBio(), existing.getBio()));
        existing.setAvatarUrl(valueOrDefault(user.getAvatarUrl(), existing.getAvatarUrl()));
        existing.setVisibilityDefault(normalizeVisibility(valueOrDefault(user.getVisibilityDefault(), existing.getVisibilityDefault())));
        existing.setCreatedAt(existing.getCreatedAt() == null ? LocalDateTime.now() : existing.getCreatedAt());
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setStreakCount(user.getStreakCount() == null ? existing.getStreakCount() : user.getStreakCount());
        existing.setLastActiveDate(user.getLastActiveDate() == null ? existing.getLastActiveDate() : user.getLastActiveDate());

        if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank()) {
            if (isBcryptHash(user.getPasswordHash())) {
                existing.setPasswordHash(user.getPasswordHash());
            } else {
                existing.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            }
        }

        User updatedUser = repository.save(existing);

        // Si el avatar ha cambiado, eliminamos el anterior del almacenamiento para evitar archivos huérfanos
        if (!Objects.equals(previousAvatarUrl, updatedUser.getAvatarUrl())) {
            fileStorageService.deleteIfManagedPath(previousAvatarUrl);
        }

        return ResponseEntity.ok(updatedUser);
    }

    // Registra la actividad diaria del usuario y actualiza su racha de días consecutivos
    @PostMapping("/{id}/daily-checkin")
    public ResponseEntity<?> dailyCheckin(@PathVariable Long id) {
        Optional<User> userOptional = repository.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        if (!authenticatedUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(error("You can only check in for your own account."));
        }

        User user = userOptional.get();
        LocalDate today = LocalDate.now();
        LocalDate lastActiveDate = user.getLastActiveDate();

        if (lastActiveDate == null) {
            user.setStreakCount(1);
            user.setLastActiveDate(today);
        } else {
            long daysBetween = ChronoUnit.DAYS.between(lastActiveDate, today);

            if (daysBetween == 0) {
                // Ya hizo check-in hoy, no se modifica nada
            } else if (daysBetween == 1) {
                user.setStreakCount((user.getStreakCount() == null ? 0 : user.getStreakCount()) + 1);
                user.setLastActiveDate(today);
            } else {
                // Si han pasado más de un día, la racha se reinicia desde 1
                user.setStreakCount(1);
                user.setLastActiveDate(today);
            }
        }

        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = repository.save(user);

        return ResponseEntity.ok(savedUser);
    }

    // Cambia el rol de un usuario; solo lo puede hacer un administrador
    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User authenticatedUser = getAuthenticatedUser();
        if (!"ADMIN".equals(authenticatedUser.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error("Admin access required."));
        }

        Optional<User> target = repository.findById(id);
        if (target.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String newRole = body.get("role");
        if (!"USER".equals(newRole) && !"ADMIN".equals(newRole)) {
            return ResponseEntity.badRequest().body(error("Role must be USER or ADMIN."));
        }

        User user = target.get();
        user.setRole(newRole);
        user.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(repository.save(user));
    }

    // Elimina la cuenta del usuario y borra en cascada todas sus recetas, colecciones, likes y demás datos
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<User> userOptional = repository.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();
        if (!authenticatedUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(error("You can only delete your own account."));
        }

        User user = userOptional.get();
        String avatarToDelete = user.getAvatarUrl();

        List<Recipe> userRecipes = recipeRepository.findByUserIdOrderByCreatedAtDescIdDesc(id);
        List<Long> recipeIds = userRecipes.stream()
                .map(Recipe::getId)
                .toList();

        // Recogemos las rutas de las imágenes de las recetas para borrarlas del almacenamiento después
        List<String> recipeImagesToDelete = userRecipes.stream()
                .map(Recipe::getImageUrl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Collection> collections = collectionRepository.findByUserId(id);
        List<Long> collectionIds = collections.stream()
                .map(Collection::getId)
                .toList();

        if (!collectionIds.isEmpty()) {
            collectionRecipeRepository.deleteByCollectionIds(collectionIds);
        }

        if (!recipeIds.isEmpty()) {
            collectionRecipeRepository.deleteByRecipeIds(recipeIds);
            likeRepository.deleteByRecipeIdIn(recipeIds);
            commentRepository.deleteByRecipeIdIn(recipeIds);
            cookedRecipeRepository.deleteByRecipeIdIn(recipeIds);
            recipeIngredientRepository.deleteByRecipeIdIn(recipeIds);
            recipeStepRepository.deleteByRecipeIdIn(recipeIds);
            recipeUtensilRepository.deleteByRecipeIds(recipeIds);
            recipeRepository.deleteAll(userRecipes);
        }

        likeRepository.deleteByUserId(id);
        commentRepository.deleteByUserId(id);
        cookedRecipeRepository.deleteByUserId(id);
        followRepository.deleteByFollowerIdOrFollowedId(id, id);
        userAchievementRepository.deleteByUserId(id);
        userChallengeRepository.deleteByUserId(id);
        collectionRepository.deleteByUserId(id);
        repository.deleteById(id);

        fileStorageService.deleteIfManagedPath(avatarToDelete);
        recipeImagesToDelete.forEach(fileStorageService::deleteIfManagedPath);

        return ResponseEntity.ok(success("Account deleted successfully."));
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationRequiredException("Authenticated user not found");
        }

        return repository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AuthenticationRequiredException("Authenticated user not found"));
    }

    // Verifica la contraseña del usuario y, si estaba en texto plano, la migra a bcrypt automáticamente
    private boolean matchesPasswordAndMigrateLegacy(User user, String rawPassword) {
        String storedPassword = user.getPasswordHash();

        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        if (isBcryptHash(storedPassword)) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        if (storedPassword.equals(rawPassword)) {
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
            user.setUpdatedAt(LocalDateTime.now());
            repository.save(user);
            return true;
        }

        return false;
    }

    // Detecta si una cadena ya es un hash bcrypt comprobando los prefijos estándar del algoritmo
    private boolean isBcryptHash(String value) {
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }

    private String valueOrDefault(String incomingValue, String currentValue) {
        return incomingValue != null ? incomingValue : currentValue;
    }

    // Normaliza el valor de visibilidad aceptando solo "public", "followers" o "private"; el resto vuelve a "public"
    private String normalizeVisibility(String visibilityDefault) {
        if (visibilityDefault == null || visibilityDefault.isBlank()) {
            return "public";
        }

        String normalized = visibilityDefault.trim().toLowerCase();
        if (normalized.equals("private")) {
            return "followers";
        }

        return normalized.equals("followers") || normalized.equals("public") ? normalized : "public";
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return body;
    }

    private Map<String, String> success(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return body;
    }
}