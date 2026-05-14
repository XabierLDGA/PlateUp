package es.ieslavereda.plateup.repository;

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
}