package es.ieslavereda.plateup.repository;

import es.ieslavereda.plateup.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    List<RecipeIngredient> findByRecipeId(Long recipeId);

    void deleteByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    void deleteByRecipeIdIn(Collection<Long> recipeIds);
}