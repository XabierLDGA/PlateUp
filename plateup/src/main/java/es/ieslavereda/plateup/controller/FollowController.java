package es.ieslavereda.plateup.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import es.ieslavereda.plateup.model.Follow;
import es.ieslavereda.plateup.model.FollowId;
import es.ieslavereda.plateup.repository.FollowRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/follows")
@CrossOrigin(origins = "*")
public class FollowController {

    private final FollowRepository repository;

    public FollowController(FollowRepository repository) {
        this.repository = repository;
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

    @PostMapping
    public ResponseEntity<Follow> create(@RequestBody Follow follow) {
        if (follow.getFollowerId() == null || follow.getFollowedId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (follow.getCreatedAt() == null) {
            follow.setCreatedAt(LocalDateTime.now());
        }

        if (follow.getStatus() == null || follow.getStatus().isBlank()) {
            follow.setStatus("accepted");
        } else {
            follow.setStatus(follow.getStatus().trim().toLowerCase());
        }

        Follow savedFollow = repository.save(follow);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFollow);
    }

    @PutMapping("/{followerId}/{followedId}")
    public ResponseEntity<Follow> update(
            @PathVariable Long followerId,
            @PathVariable Long followedId,
            @RequestBody Follow updatedFollow
    ) {
        FollowId id = new FollowId(followerId, followedId);

        return repository.findById(id)
                .map(existingFollow -> {
                    if (updatedFollow.getStatus() != null && !updatedFollow.getStatus().isBlank()) {
                        existingFollow.setStatus(updatedFollow.getStatus().trim().toLowerCase());
                    }

                    if (updatedFollow.getCreatedAt() != null) {
                        existingFollow.setCreatedAt(updatedFollow.getCreatedAt());
                    }

                    Follow savedFollow = repository.save(existingFollow);
                    return ResponseEntity.ok(savedFollow);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{followerId}/{followedId}/accept")
    public ResponseEntity<Follow> acceptRequest(@PathVariable Long followerId, @PathVariable Long followedId) {
        FollowId id = new FollowId(followerId, followedId);

        return repository.findById(id)
                .map(existingFollow -> {
                    existingFollow.setStatus("accepted");
                    Follow savedFollow = repository.save(existingFollow);
                    return ResponseEntity.ok(savedFollow);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{followerId}/{followedId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long followerId, @PathVariable Long followedId) {
        FollowId id = new FollowId(followerId, followedId);

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Follow deleted."));
    }
}