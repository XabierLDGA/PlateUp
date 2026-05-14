package es.ieslavereda.plateup.controller;

import es.ieslavereda.plateup.exception.AuthenticationRequiredException;
import es.ieslavereda.plateup.model.Follow;
import es.ieslavereda.plateup.model.FollowId;
import es.ieslavereda.plateup.model.User;
import es.ieslavereda.plateup.repository.FollowRepository;
import es.ieslavereda.plateup.repository.UserRepository;
import es.ieslavereda.plateup.service.AchievementUnlockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/follows")
@CrossOrigin(origins = "*")
public class FollowController {

    private final FollowRepository repository;
    private final AchievementUnlockService achievementUnlockService;
    private final UserRepository userRepository;

    public FollowController(
            FollowRepository repository,
            AchievementUnlockService achievementUnlockService,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.achievementUnlockService = achievementUnlockService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Follow> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{followerId}/{followedId}")
    public Optional<Follow> getById(@PathVariable Long followerId, @PathVariable Long followedId) {
        return repository.findById(new FollowId(followerId, followedId));
    }

    @GetMapping("/requests/{userId}")
    public List<Follow> getPendingRequestsForUser(@PathVariable Long userId) {
        return repository.findByFollowedIdAndStatus(userId, "pending");
    }

    @GetMapping("/following/{userId}")
    public List<Follow> getFollowingByUser(@PathVariable Long userId) {
        return repository.findByFollowerIdAndStatus(userId, "accepted");
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Follow follow) {
        User authenticatedUser = getAuthenticatedUser();

        if (follow.getFollowedId() == null) {
            return ResponseEntity.badRequest().body("followedId is required.");
        }

        follow.setFollowerId(authenticatedUser.getId());

        if (follow.getCreatedAt() == null) {
            follow.setCreatedAt(LocalDateTime.now());
        }

        if (follow.getStatus() == null || follow.getStatus().isBlank()) {
            follow.setStatus("accepted");
        } else {
            follow.setStatus(follow.getStatus().trim().toLowerCase());
        }

        Follow savedFollow = repository.save(follow);

        if ("accepted".equals(savedFollow.getStatus())) {
            achievementUnlockService.checkFollowerAchievements(savedFollow.getFollowedId());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedFollow);
    }

    @PutMapping("/{followerId}/{followedId}")
    public ResponseEntity<?> update(
            @PathVariable Long followerId,
            @PathVariable Long followedId,
            @RequestBody Follow updatedFollow
    ) {
        FollowId id = new FollowId(followerId, followedId);

        return repository.findById(id)
                .map(existingFollow -> {
                    User authenticatedUser = getAuthenticatedUser();

                    if (!authenticatedUser.getId().equals(existingFollow.getFollowedId())
                            && !authenticatedUser.getId().equals(existingFollow.getFollowerId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("You can only update your own follow relationships.");
                    }

                    if (updatedFollow.getStatus() != null && !updatedFollow.getStatus().isBlank()) {
                        existingFollow.setStatus(updatedFollow.getStatus().trim().toLowerCase());
                    }

                    if (updatedFollow.getCreatedAt() != null) {
                        existingFollow.setCreatedAt(updatedFollow.getCreatedAt());
                    }

                    Follow savedFollow = repository.save(existingFollow);

                    if ("accepted".equals(savedFollow.getStatus())) {
                        achievementUnlockService.checkFollowerAchievements(savedFollow.getFollowedId());
                    }

                    return ResponseEntity.ok(savedFollow);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{followerId}/{followedId}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable Long followerId, @PathVariable Long followedId) {
        FollowId id = new FollowId(followerId, followedId);

        return repository.findById(id)
                .map(existingFollow -> {
                    User authenticatedUser = getAuthenticatedUser();

                    if (!authenticatedUser.getId().equals(existingFollow.getFollowedId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("You can only accept requests sent to your account.");
                    }

                    existingFollow.setStatus("accepted");
                    Follow savedFollow = repository.save(existingFollow);
                    achievementUnlockService.checkFollowerAchievements(savedFollow.getFollowedId());

                    return ResponseEntity.ok(savedFollow);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{followerId}/{followedId}")
    public ResponseEntity<?> delete(@PathVariable Long followerId, @PathVariable Long followedId) {
        FollowId id = new FollowId(followerId, followedId);

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        User authenticatedUser = getAuthenticatedUser();

        if (!authenticatedUser.getId().equals(followerId) && !authenticatedUser.getId().equals(followedId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "You can only delete your own follow relationships."));
        }

        repository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Follow deleted."));
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationRequiredException("Authenticated user not found");
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AuthenticationRequiredException("Authenticated user not found"));
    }
}