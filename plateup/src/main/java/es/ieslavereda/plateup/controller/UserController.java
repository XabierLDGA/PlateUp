package es.ieslavereda.plateup.controller;

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

    @GetMapping
    public List<User> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String identifier = request.getIdentifier() == null ? "" : request.getIdentifier().trim();
        String password = request.getPassword() == null ? "" : request.getPassword().trim();

        if (identifier.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(error("Username/email and password are required."));
        }

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

        if (!Objects.equals(previousAvatarUrl, updatedUser.getAvatarUrl())) {
            fileStorageService.deleteIfManagedPath(previousAvatarUrl);
        }

        return ResponseEntity.ok(updatedUser);
    }

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
                // Ya hizo check-in hoy
            } else if (daysBetween == 1) {
                user.setStreakCount((user.getStreakCount() == null ? 0 : user.getStreakCount()) + 1);
                user.setLastActiveDate(today);
            } else {
                user.setStreakCount(1);
                user.setLastActiveDate(today);
            }
        }

        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = repository.save(user);

        return ResponseEntity.ok(savedUser);
    }

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
            throw new RuntimeException("Authenticated user not found");
        }

        return repository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

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

    private boolean isBcryptHash(String value) {
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }

    private String valueOrDefault(String incomingValue, String currentValue) {
        return incomingValue != null ? incomingValue : currentValue;
    }

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