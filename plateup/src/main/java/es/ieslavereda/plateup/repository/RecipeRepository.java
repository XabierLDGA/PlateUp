package es.ieslavereda.plateup.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import es.ieslavereda.plateup.model.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByOrderByCreatedAtDescIdDesc();

    List<Recipe> findByUserIdOrderByCreatedAtDescIdDesc(Long userId);

    long countByUserId(Long userId);

    Optional<Recipe> findTopByUserIdOrderByCreatedAtDescIdDesc(Long userId);

    @Query("SELECT r.userId FROM Recipe r WHERE r.id = :recipeId")
    Optional<Long> findUserIdById(@Param("recipeId") Long recipeId);

    @Query("SELECT r FROM Recipe r WHERE r.userId = :userId " +
           "OR r.userId IN (SELECT f.followedId FROM Follow f WHERE f.followerId = :userId AND f.status = 'accepted') " +
           "ORDER BY r.createdAt DESC, r.id DESC")
    Page<Recipe> findFeedForUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT r FROM Recipe r WHERE (r.userId = :userId " +
           "OR r.userId IN (SELECT f.followedId FROM Follow f WHERE f.followerId = :userId AND f.status = 'accepted')) " +
           "AND r.category = :category " +
           "ORDER BY r.createdAt DESC, r.id DESC")
    Page<Recipe> findFeedForUserAndCategory(@Param("userId") Long userId, @Param("category") String category, Pageable pageable);
}