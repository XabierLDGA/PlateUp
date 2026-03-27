package es.ieslavereda.plateup.repository;

import es.ieslavereda.plateup.model.CookedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CookedRecipeRepository extends JpaRepository<CookedRecipe, Long> {

    List<CookedRecipe> findByUserId(Long userId);

    Optional<CookedRecipe> findByUserIdAndRecipeId(Long userId, Long recipeId);

    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);

    void deleteByUserIdAndRecipeId(Long userId, Long recipeId);

    void deleteByUserId(Long userId);

    void deleteByRecipeIdIn(Collection<Long> recipeIds);
}