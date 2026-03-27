package es.ieslavereda.plateup.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            CollectionRecipeRepository collectionRecipeRepository
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

        if (user.getPasswordHash() == null || !user.getPasswordHash().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(error("The username/email or password is incorrect."));
        }

        return ResponseEntity.ok(user);
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
        user.setPasswordHash(password);
        user.setDisplayName(displayName);
        user.setBio(bio);
        user.setAvatarUrl(avatarUrl);
        user.setVisibilityDefault(visibilityDefault);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User createdUser = repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody User user) {
        Optional<User> existingOptional = repository.findById(id);

        if (existingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existing = existingOptional.get();

        existing.setUsername(valueOrDefault(user.getUsername(), existing.getUsername()));
        existing.setEmail(valueOrDefault(user.getEmail(), existing.getEmail()));
        existing.setPasswordHash(valueOrDefault(user.getPasswordHash(), existing.getPasswordHash()));
        existing.setDisplayName(valueOrDefault(user.getDisplayName(), existing.getDisplayName()));
        existing.setBio(valueOrDefault(user.getBio(), existing.getBio()));
        existing.setAvatarUrl(valueOrDefault(user.getAvatarUrl(), existing.getAvatarUrl()));
        existing.setVisibilityDefault(normalizeVisibility(valueOrDefault(user.getVisibilityDefault(), existing.getVisibilityDefault())));
        existing.setCreatedAt(existing.getCreatedAt() == null ? LocalDateTime.now() : existing.getCreatedAt());
        existing.setUpdatedAt(LocalDateTime.now());

        User updatedUser = repository.save(existing);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<User> userOptional = repository.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Recipe> userRecipes = recipeRepository.findByUserIdOrderByCreatedAtDescIdDesc(id);
        List<Long> recipeIds = userRecipes.stream()
                .map(Recipe::getId)
                .toList();

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

        return ResponseEntity.ok(success("Account deleted successfully."));
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