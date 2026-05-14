package es.ieslavereda.plateup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import es.ieslavereda.plateup.model.Ingredient;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    @Query("SELECT i FROM Ingredient i WHERE i.id IN (SELECT ri.ingredientId FROM RecipeIngredient ri WHERE ri.recipeId = :recipeId)")
    List<Ingredient> findByRecipeId(@Param("recipeId") Long recipeId);
}
